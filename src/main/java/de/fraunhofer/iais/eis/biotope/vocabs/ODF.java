package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;

/**
 * Created by christian on 23.11.16.
 */
public class ODF {

    private static ValueFactory vf = new MemValueFactory();

    private final static String NS = "http://eis-biotope.iais.fraunhofer.de/odf#";

    // Classes
    public static final String Object = NS + "Object";
    public static final String InfoItem = NS + "InfoItem";

    // Properties
    public static final String infoitem = NS + "infoitem";
}
