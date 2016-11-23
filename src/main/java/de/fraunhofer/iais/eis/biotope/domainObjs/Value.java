package de.fraunhofer.iais.eis.biotope.domainObjs;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "value")
public class Value {

    private String datetime;

    public String getDatetime() {
        return datetime;
    }

    @XmlAttribute(name = "dateTime")
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
