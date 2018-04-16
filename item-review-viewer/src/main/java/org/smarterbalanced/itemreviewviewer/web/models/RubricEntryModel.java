package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="rubric")
@XmlAccessorType(XmlAccessType.NONE)
public class RubricEntryModel {
    @XmlElement(name="name")
    private String name;
    @XmlAttribute(name="scorepoint")
    private String scorepoint;
    @XmlElement(name="val")
    private String val;

    public RubricEntryModel(){

    }

    public RubricEntryModel( String val, String name, String scorepoint){
        this.scorepoint = scorepoint;
        this.name = name;
        this.val = val;
    }

    @JsonProperty("name")
    public String getName() { return name; }
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("scorepoint")
    public String getVal() {
        return val;
    }
    public void setVal(String value) {
        this.val = value;
    }

    @JsonProperty("val")
    public String getScorepoint() {
        return scorepoint;
    }
    public void setScorepoint(String value) {
        this.scorepoint = value;
    }

}

