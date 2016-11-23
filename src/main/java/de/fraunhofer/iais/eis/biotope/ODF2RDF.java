package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.domainObjs.Object;
import de.fraunhofer.iais.eis.biotope.domainObjs.Objects;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
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

public class ODF2RDF {

    public void odf2rdf(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, JAXBException {

        Objects beans = JAXB.unmarshal(new StringReader(xml), Objects.class);

        ValueFactory vf = new MemValueFactory();
        IRI objectBaseIri = vf.createIRI("http://eis-biotope.iais.fraunhofer.de/biotope.sntiotlab.lu/obj/");
        IRI infoItemBaseIri = vf.createIRI("http://eis-biotope.iais.fraunhofer.de/biotope.sntiotlab.lu/infoitem/");

        for (Object obj : beans.getObjects()) {
            Model objModel = obj.serialize(vf, objectBaseIri, infoItemBaseIri);
            dumpModel(objModel);
        }
    }

    private void dumpModel(Model model) {
        RDFWriter rdfWriter = new TurtleWriter(System.out);
        rdfWriter.startRDF();
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
    }

}
