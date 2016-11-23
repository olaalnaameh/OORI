package de.fraunhofer.iais.eis.biotope.annotations;

import org.eclipse.rdf4j.model.IRI;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface RdfType {

    String[] value();

}
