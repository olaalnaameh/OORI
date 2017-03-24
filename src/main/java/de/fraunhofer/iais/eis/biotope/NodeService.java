package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestCreationException;
import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestResponseException;
import de.fraunhofer.iais.eis.biotope.exceptions.OMIRequestSendException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
class NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeService.class);

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private Collection<OmiNode> omiNodes = new HashSet<>();

    @Autowired
    private OdfRdfConverter odfRdfConverter;

    @Autowired
    private OdfRdfRepository odfRdfRepository;

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
            logger.info("setting callback to: '" +ConfigurationProvider.getCallbackUrl()+ "'");
            callback.setValue(ConfigurationProvider.getCallbackUrl());

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

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(subscriptionResponse.getEntity().getContent());

            Node returnNode = doc.getElementsByTagName("omi:return").item(0);

            int omiStatus = getOmiDocumentReturnCode(doc);
            String omiDescription = returnNode.getAttributes().getNamedItem("description").getNodeValue();

            if (omiStatus != HttpStatus.SC_OK) {
                throw new OMIRequestResponseException("O-MI response status: " +omiStatus+ ", description: '" +omiDescription+ "'");
            }

            String subscriptionId = doc.getElementsByTagName("omi:requestID").item(0).getTextContent();

            logger.info("Subscription sent successfully");
            return subscriptionId;
        }
        catch (SAXException | ParserConfigurationException | IOException e) {
            throw new OMIRequestCreationException("Error parsing O-MI response", e);
        }
    }

    private int getOmiDocumentReturnCode(Document doc) {
        Node returnNode = doc.getElementsByTagName("omi:return").item(0);
        return Integer.parseInt(returnNode.getAttributes().getNamedItem("returnCode").getNodeValue());
    }

    public void persistOmiMessageContent(String omiMessage) {
        logger.info("O-MI value changed");

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(omiMessage.getBytes(StandardCharsets.UTF_8)));
            int omiRetCode = getOmiDocumentReturnCode(doc);
            if (omiRetCode == HttpStatus.SC_OK) {
                String odfStructure = innerXml(doc.getElementsByTagName("omi:msg").item(0));
                persistOdfStructure(odfStructure);
            }
            else {
                logger.warn("O-MI message has error code " +omiRetCode+ ". Ignoring the message.");
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new OMIRequestCreationException("Error parsing O-MI message", e);
        }
    }

    private String innerXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS)node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString();
    }

    private void persistOdfStructure(String odfStructure) {
        Model odfData = odfRdfConverter.odf2rdf(new ByteArrayInputStream(odfStructure.getBytes(StandardCharsets.UTF_8)));
        odfRdfRepository.persist(odfData);
    }

    public Collection<OmiNode> getOmiNodes() {
        return omiNodes;
    }

    public void baselineSync(OmiNode omiNode) {
        HttpResponse response = sendRequest(omiNode.getUrl(), omiNode.getSubscriptionXMLRequest());

        try {
            String odfContent = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            persistOmiMessageContent(odfContent);
        }
        catch (IOException e) {
            throw new OMIRequestResponseException("Error reading O-MI response conent");
        }
    }

}
