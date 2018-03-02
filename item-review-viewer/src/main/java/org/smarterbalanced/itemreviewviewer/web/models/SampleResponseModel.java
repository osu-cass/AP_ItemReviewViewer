package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SampleResponseModel {
    protected String purpose;
    protected String scorePoint;
    protected String name;
    protected String sampleContent;

    @JsonProperty("purpose")
    public String getPurpose() {
        return purpose;
    }

    void setPurpose(String purpose){
        this.purpose = purpose;
    }

    @JsonProperty("scorePoint")
    public String getScorePoint() {
        return scorePoint;
    }

    void setScorePoint(String scorePoint){
        this.scorePoint = scorePoint;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    void setName(String name){
        this.name = name;
    }

    @JsonProperty("sampleContent")
    public String getSampleContent() {
        return sampleContent;
    }

    void setSampleContent(String sampleContent){
        this.sampleContent = sampleContent;
    }
}
