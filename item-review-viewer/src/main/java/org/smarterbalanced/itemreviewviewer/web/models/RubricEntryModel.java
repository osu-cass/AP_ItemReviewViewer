package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RubricEntryModel {

    protected String name;
    protected String val;
    protected String scorepoint;

    public RubricEntryModel(String name, String val, String scorepoint){
        this.name = name;
        this.val = val;
        this.scorepoint = scorepoint;
    }

    @JsonProperty("name")
    public String getName() { return name; }
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("val")
    public String getVal() {
        return val;
    }
    public void setVal(String value) {
        this.val = value;
    }

    @JsonProperty("scorepoint")
    public String getScorepoint() {
        return scorepoint;
    }
    public void setScorepoint(String value) {
        this.scorepoint = value;
    }

}

