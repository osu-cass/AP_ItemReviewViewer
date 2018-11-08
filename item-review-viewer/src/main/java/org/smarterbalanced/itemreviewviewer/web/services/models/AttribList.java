package org.smarterbalanced.itemreviewviewer.web.services.models;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class AttribList {
    @XmlElement (name = "attrib")
    @JsonProperty ("attrib")
    private Attrib[] attrib;

    public Attrib[] getAttrib() {
        return attrib;
    }

    public void setAttrib(Attrib[] attrib) {
        this.attrib = attrib;
    }
}
