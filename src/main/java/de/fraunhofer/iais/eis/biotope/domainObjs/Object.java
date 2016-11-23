package de.fraunhofer.iais.eis.biotope.domainObjs;

import de.fraunhofer.iais.eis.biotope.annotations.RdfProperty;
import de.fraunhofer.iais.eis.biotope.vocabs.ODF;
import de.fraunhofer.iais.eis.biotope.annotations.RdfType;
import de.fraunhofer.iais.eis.biotope.vocabs.SKOS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@RdfType(ODF.Object)
@XmlRootElement
public class Object {

    @RdfProperty(SKOS.notation)
    private String id;

    @RdfProperty(ODF.infoitem)
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
}
