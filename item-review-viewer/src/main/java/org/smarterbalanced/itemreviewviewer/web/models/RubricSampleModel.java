package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RubricSampleModel {
    protected String maxValue;
    protected String minValue;
    protected List<SampleResponseModel> sampleResponses;

    public RubricSampleModel(String maxValue, String minValue, List<SampleResponseModel> sampleResponses){
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.sampleResponses = sampleResponses;
    }

    @JsonProperty("maxValue")
    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    @JsonProperty("minValue")
    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    @JsonProperty("sampleResponses")
    public List<SampleResponseModel> getSampleResponses() {
        return sampleResponses;
    }

    public void setSampleResponses(List<SampleResponseModel> sampleResponses) {
        this.sampleResponses = sampleResponses;
    }
}
