package de.fraunhofer.iais.eis.biotope;

import de.fraunhofer.iais.eis.biotope.domainObjs.Value;
import de.fraunhofer.iais.eis.jrdfb.JrdfbException;
import de.fraunhofer.iais.eis.jrdfb.serializer.RdfSerializer;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by christian on 24.03.17.
 */
public class SerializationTest {

    private RdfSerializer serializer;

    @Before
    public void setUp() {
        serializer = new RdfSerializer(Value.class);
    }

    @Test
    public void serializeValue() throws JrdfbException {
        Value value = new Value();
        value.setDatavalue("metadata to be added");
        value.setDatetime("2016-11-22T09:48:15.946+01:00");
        value.setType("xs:string");

        String rdf = serializer.serialize(value);
        System.out.println(rdf);
    }

}
