package de.fraunhofer.iais.eis.biotope;

import org.junit.Test;

import java.io.InputStream;

public class OMI2RDFTest {

    private ODF2RDF odf2rdf = new ODF2RDF();

    @Test
    public void omi2rdf_simple()  {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/callbackMessage.xml");
        odf2rdf.odf2rdf(odfStructure);

        //todo: add assertions
    }

    @Test
    public void omi2rdf_metadata_and_description()  {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/objTreeMetadataAndDescription.xml");
        odf2rdf.odf2rdf(odfStructure);

        //todo: add assertions
    }

    @Test
    public void omi2rdf_multi_objects()  {
        InputStream odfStructure = getClass().getResourceAsStream("/resources/objTreeMultiObjects.xml");
        odf2rdf.odf2rdf(odfStructure);

        //todo: add assertions
    }

}
