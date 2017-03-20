package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.domainObjs.Object;
import de.fraunhofer.iais.eis.biotope.domainObjs.Objects;
import de.fraunhofer.iais.eis.jrdfb.serializer.RdfSerializer;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ModelFactory;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
import org.springframework.stereotype.Component;
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
import java.io.*;
import java.nio.charset.Charset;

@Component
public class OdfRdfConverter {

    private final static String BASE_URI = "http://eis-biotope.iais.fraunhofer.de/";

    private String hostname = "localhost";

    public Model odf2rdf(InputStream odfStructure) {
        Objects beans = JAXB.unmarshal(odfStructure, Objects.class);

        ValueFactory vf = new MemValueFactory();
        String objectBaseIri = BASE_URI + hostname + "/obj/";
        String infoItemBaseIri = BASE_URI + hostname + "/infoitem/";

        Model model = new ModelBuilder().build();

        RdfSerializer rdfSerializer = new RdfSerializer();
        String rdf = rdfSerializer.serialize(beans);
        model.addAll(rdf)

        /*
        beans.getObjects().forEach(objectBean -> model.addAll(objectBean.serialize(vf, objectBaseIri, infoItemBaseIri)));
        */
        //dumpModel(model);

        return model;
    }

    private void dumpModel(Model model) {
        RDFWriter rdfWriter = new TurtleWriter(System.out);
        rdfWriter.startRDF();
        model.forEach(statment -> rdfWriter.handleStatement(statment));
        rdfWriter.endRDF();
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
