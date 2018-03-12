package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RubricEntryModel {

    protected String scorepoint;
    protected String name;
    protected String val;



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

