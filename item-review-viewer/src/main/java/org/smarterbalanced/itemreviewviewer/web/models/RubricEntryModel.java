package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;

public class RubricEntryModel {

    private String name;
    private String scorepoint;
    private String val;

    public RubricEntryModel(){

    }

    public RubricEntryModel( String val, String name, String scorepoint){
        this.scorepoint = scorepoint;
        this.name = name;
        this.val = val;
    }

    @XmlElement(name="name")
    @JsonProperty("name")
    public String getName() { return name; }
    public void setName(String value) {
        this.name = value;
    }


    @XmlAttribute(name="scorepoint")
    @JsonProperty("scorepoint")
    public String getVal() {
        return val;
    }
    public void setVal(String value) {
        this.val = value;
    }

    @XmlElement(name="val")
    @JsonProperty("val")
    public String getScorepoint() {
        return scorepoint;
    }
    public void setScorepoint(String value) {
        this.scorepoint = value;
    }

}

