package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.vocabs.NS;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Namespaces;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashSet;

@XmlRootElement
public class Object {

    private String id;

    private Collection<InfoItem> infoItems;

    public String getId() {
        return id;
    }

    @XmlElement(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public Collection<InfoItem> getInfoItems() {
        return infoItems;
    }

    @XmlElement(name = "InfoItem")
    public void setInfoItems(Collection<InfoItem> infoItems) {
        this.infoItems = infoItems;
    }

    public Model serialize(ValueFactory vf, IRI objectBaseIri, IRI infoItemBaseIri) {

        IRI subject = vf.createIRI(objectBaseIri.toString() + id);

        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("dct", NS.DCT)
                .setNamespace("odf", NS.ODF)
                .setNamespace("rdf", RDF.NAMESPACE)

                .subject(subject)
                .add("rdf:type", "odf:Object")
                .add("skos:notation", id);

        Collection<Model> infoItemModels = new HashSet<>();
        infoItems.forEach(infoitem -> infoItemModels.add(infoitem.serialize(vf, infoItemBaseIri)));

        infoItemModels.forEach(model -> {
            Resource infoItemId = model.iterator().next().getSubject();
            builder.add("odf:infoitem", infoItemId);
        });

        Model objectModel = builder.build();
        infoItemModels.forEach(infoItemModel -> objectModel.addAll(infoItemModel));

        return objectModel;
    }

}
