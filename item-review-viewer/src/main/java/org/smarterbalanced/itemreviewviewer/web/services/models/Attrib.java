package org.smarterbalanced.itemreviewviewer.web.services.models;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Attrib {
    @XmlElement (name = "name")
    private String name;
    @XmlElement (name = "val")
    private String val;
    @XmlElement (name = "desc")
    private String desc;
    @JsonProperty ("desc")
    public String getDesc() {
        return desc;
    }
    @JsonProperty("val")
    public String getVal() {
        return val;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
