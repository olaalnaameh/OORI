package de.fraunhofer.iais.eis.biotope;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class OMI2RDFTest {

    private ODF2RDF ODF2Rdf = new ODF2RDF();

    @Test
    public void omi2rdf() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, JAXBException {
        InputStream msg = getClass().getResourceAsStream("/resources/callbackMessage.xml");

        StringWriter writer = new StringWriter();
        IOUtils.copy(msg, writer, Charset.defaultCharset());
        String theString = writer.toString();

        ODF2Rdf.odf2rdf(theString);
    }

}
