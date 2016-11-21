package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestCreationException;
import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestResponseException;
import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestSendException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Component
class NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeService.class);
    private final String CALLBACK_METHOD_PATH = "/callback";
    private final String DEFAULT_PROTOCOL = "http";
    private final String DEFAULT_PORT = "9000";
    private final String DEFAULT_HOSTNAME = "localhost";

    private Collection<OmiNode> omiNodes = new HashSet<>();
    private String callbackUrl = CALLBACK_METHOD_PATH;

    public NodeService() {
        String protocol = System.getenv("PROTOCOL");
        protocol = protocol == null ? DEFAULT_PROTOCOL : protocol;

        String hostname = System.getenv("HOSTNAME");
        hostname = hostname == null ? DEFAULT_HOSTNAME : hostname;

        String port = System.getenv("PORT");
        port = port == null ? DEFAULT_PORT : port;

        callbackUrl = protocol + "://" + hostname + ":" + port + CALLBACK_METHOD_PATH;
    }

    public void addNode(OmiNode omiNode) {
        omiNodes.add(omiNode);
    }

    public void subscribe(OmiNode omiNode) {
        logger.debug("subscribing omiNode " + omiNode.toString());
        String subscriptionRequest = createSubscriptionRequest(omiNode.getSubscriptionXMLRequest());

        HttpResponse response = sendRequest(omiNode.getUrl(), subscriptionRequest);
        omiNode.setSubscriptionId(getSubscriptionRequestId(response));
    }

    private String createSubscriptionRequest(String subscriptionXMLRequest) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(subscriptionXMLRequest.getBytes());
            Document doc = db.parse(is);
            doc.getChildNodes();

            Attr ttl = doc.createAttribute("ttl");
            ttl.setValue("-1");
            Attr interval = doc.createAttribute("interval");
            interval.setValue("-1");
            Attr callback = doc.createAttribute("callback");
            callback.setValue(callbackUrl);

            Node envelopeNode = doc.getElementsByTagName("omi:omiEnvelope").item(0);
            envelopeNode.getAttributes().setNamedItem(ttl);

            Node readNode = doc.getElementsByTagName("omi:read").item(0);
            readNode.getAttributes().setNamedItem(interval);
            readNode.getAttributes().setNamedItem(callback);

            return serializeDocument(doc);
        }
        catch (TransformerException | SAXException | ParserConfigurationException | IOException e) {
            throw new OMIRequestCreationException("Error creating O-MI subscription request", e);
        }
    }

    private String serializeDocument(Document doc) throws TransformerException {
        StringWriter writer = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        writer.flush();
        return writer.toString();
    }

    private HttpResponse sendRequest(URL url, String request) {
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            HttpPost post = new HttpPost(url.toURI());
            post.setEntity(new StringEntity(request));
            return client.execute(post);
        }
        catch (URISyntaxException | IOException e) {
            throw new OMIRequestSendException("Error sending O-MI request", e);
        }
    }

    private String getSubscriptionRequestId(HttpResponse subscriptionResponse) {
        int httpStatus = subscriptionResponse.getStatusLine().getStatusCode();
        if (httpStatus != HttpStatus.SC_OK) {
            throw new OMIRequestResponseException("HTTP response status: " +httpStatus);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(subscriptionResponse.getEntity().getContent());

            Node returnNode = doc.getElementsByTagName("omi:return").item(0);

            int omiStatus = Integer.parseInt(returnNode.getAttributes().getNamedItem("returnCode").getNodeValue());
            String omiDescription = returnNode.getAttributes().getNamedItem("description").getNodeValue();

            if (omiStatus != HttpStatus.SC_OK) {
                throw new OMIRequestResponseException("O-MI response status: " +omiStatus+ ", description: '" +omiDescription+ "'");
            }

            return doc.getElementsByTagName("omi:requestID").item(0).getTextContent();
        }
        catch (SAXException | ParserConfigurationException | IOException e) {
            throw new OMIRequestCreationException("Error parsing O-MI response", e);
        }
    }

    public void unsubscribe(OmiNode omiNode) {

    }

    public void valueChanged(String omiMessage) {
        System.out.println("changed: '" +omiMessage+ "'");

        // todo: convert to RDF and update repository
    }

    public Collection<OmiNode> getOmiNodes() {
        return omiNodes;
    }

}
