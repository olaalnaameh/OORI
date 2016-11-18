package de.fhg.ids.app.datadump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by christian on 17.11.16.
 */
public class OmiNode {

    private final static Logger logger = LoggerFactory.getLogger(OmiNode.class);

    private URL url;
    private String subscriptionXMLRequest;

    public static OmiNode build(URL url, String subscriptionXMLRequest) {
        validateXml(subscriptionXMLRequest);
        return new OmiNode(url, subscriptionXMLRequest);
    }

    private OmiNode(URL url, String subscriptionXMLRequest) {
        this.url = url;
        this.subscriptionXMLRequest = subscriptionXMLRequest;
    }

    private static void validateXml(String subscriptionXMLRequest)  {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream( subscriptionXMLRequest.getBytes() );
            db.parse(is);
        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            logger.info("Invalid O-MI request");
            throw new OMIRequestParseException(e);
        }
    }

    public URL getUrl() {
        return url;
    }

    public String getSubscriptionXMLRequest() {
        return subscriptionXMLRequest;
    }

    @Override
    public String toString() {
        return "OmiNode: '" +url.toString()+ "', request: '" +subscriptionXMLRequest+ "'";
    }
}
