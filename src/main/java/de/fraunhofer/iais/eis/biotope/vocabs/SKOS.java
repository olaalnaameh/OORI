package de.fraunhofer.iais.eis.biotope.vocabs;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;

public class SKOS {

    private static ValueFactory vf = new MemValueFactory();

    private final static String NS = "http://www.w3.org/2004/02/skos/core#";

    public static final String notation = NS + "notation";

}
