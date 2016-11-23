package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashSet;

@XmlRootElement
public class InfoItem {

    private String name;
    private Collection<Value> values;

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public Collection<Value> getValues() {
        return values;
    }

    @XmlElement(name = "value")
    public void setValues(Collection<Value> values) {
        this.values = values;
    }

    public Model serialize(ValueFactory vf, String infoItemBaseIri) {
        IRI subject = vf.createIRI(infoItemBaseIri + name);

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)

                .subject(subject)
                .add("rdf:type", "odf:InfoItem")
                .add("dct:title", name);

        Collection<Model> valueModels = new HashSet<>();
        values.forEach(value -> valueModels.add(value.serialize(vf)));

        valueModels.forEach(model -> {
            Resource valueId = model.iterator().next().getSubject();
            builder.add("odf:value", valueId);
        });

        Model infoItemModel = builder.build();
        valueModels.forEach(valueModel -> infoItemModel.addAll(valueModel));

        return infoItemModel;
    }
}
