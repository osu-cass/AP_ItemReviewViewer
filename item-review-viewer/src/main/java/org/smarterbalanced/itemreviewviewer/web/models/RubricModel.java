package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RubricModel {
    protected String language;
    protected List<RubricEntryModel> rubricEntries;
    protected List<RubricSampleModel> samples;

    public RubricModel(String language, List<RubricEntryModel> rubricEntries, List<RubricSampleModel> samples) {
        this.language = language;
        this.rubricEntries = rubricEntries;
        this.samples = samples;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("rubricEntries")
    public List<RubricEntryModel> getRubricEntries() {
        return rubricEntries;
    }

    public void setRubricEntries(List<RubricEntryModel> rubricEntries) {
        this.rubricEntries = rubricEntries;
    }

    @JsonProperty("samples")
    public List<RubricSampleModel> getSamples() {
        return samples;
    }

    public void setSamples(List<RubricSampleModel> samples) {
        this.samples = samples;
    }
}
