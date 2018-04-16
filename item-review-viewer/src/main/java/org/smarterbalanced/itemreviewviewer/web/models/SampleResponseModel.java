package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;

public class SampleResponseModel {
    protected String purpose;
    protected String scorePoint;
    protected String name;
    protected String sampleContent;

    public SampleResponseModel(){

    }

    public SampleResponseModel(String purpose, String scorePoint, String name, String sampleContent){
        this.purpose = purpose;
        this.scorePoint = scorePoint;
        this.name = name;
        this.sampleContent = sampleContent;
    }

    @XmlElement(name="purpose")
    @JsonProperty("purpose")
    public String getPurpose() {
        return purpose;
    }

    void setPurpose(String purpose){
        this.purpose = purpose;
    }

    @XmlElement(name="scorepoint")
    @JsonProperty("scorePoint")
    public String getScorePoint() {
        return scorePoint;
    }

    void setScorePoint(String scorePoint){
        this.scorePoint = scorePoint;
    }

    @XmlElement(name="name")
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    void setName(String name){
        this.name = name;
    }

    @XmlElement(name="sampleContent")
    @JsonProperty("sampleContent")
    public String getSampleContent() {
        return sampleContent;
    }

    void setSampleContent(String sampleContent){
        this.sampleContent = sampleContent;
    }
}
