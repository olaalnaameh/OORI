package de.fraunhofer.iais.eis.biotope.domainObjs;

import org.eclipse.rdf4j.model.Resource;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

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
}
