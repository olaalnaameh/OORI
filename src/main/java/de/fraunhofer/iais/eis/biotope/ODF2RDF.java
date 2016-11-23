package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.domainObjs.Object;
import de.fraunhofer.iais.eis.biotope.domainObjs.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by christian on 22.11.16.
 */
public class ODF2RDF {

    public void odf2rdf(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, JAXBException {

        Objects r = JAXB.unmarshal(new StringReader(xml), Objects.class);

        System.out.println("xxx");

    }



}
