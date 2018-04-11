package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class RubricSampleModel {
    private String maxValue;
    private String minValue;
    private List<SampleResponseModel> sampleResponses;

    public RubricSampleModel(){

    }

    public RubricSampleModel(String maxValue, String minValue, List<SampleResponseModel> sampleResponses){
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.sampleResponses = sampleResponses;
    }

    @XmlAttribute(name="maxval")
    @JsonProperty("maxValue")
    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    @XmlAttribute(name="minval")
    @JsonProperty("minValue")
    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    @XmlElement(name="sample")
    @JsonProperty("sampleResponses")
    public List<SampleResponseModel> getSampleResponses() {
        return sampleResponses;
    }

    public void setSampleResponses(List<SampleResponseModel> sampleResponses) {
        this.sampleResponses = sampleResponses;
    }
}
