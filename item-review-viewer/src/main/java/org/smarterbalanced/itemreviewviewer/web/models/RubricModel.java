package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="rubriclist")
@XmlAccessorType(XmlAccessType.NONE)
public class RubricModel {

    @XmlElement(name="language")
    @JsonProperty("language")
    protected String language;

    @XmlElement(name="rubric")
    @JsonProperty("rubricEntries")
    protected List<RubricEntryModel> rubricEntries;

    @XmlElement(name="samplelist")
    @JsonProperty("samples")
    protected List<RubricSampleModel> samples;

    public RubricModel(){

    }

    public RubricModel(String language, List<RubricEntryModel> rubricEntries, List<RubricSampleModel> samples) {
        this.language = language;
        this.rubricEntries = rubricEntries;
        this.samples = samples;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public List<RubricEntryModel> getRubricEntries() {
        return rubricEntries;
    }

    public void setRubricEntries(List<RubricEntryModel> rubricEntries) {
        this.rubricEntries = rubricEntries;
    }

    public List<RubricSampleModel> getSamples() {
        return samples;
    }

    public void setSamples(List<RubricSampleModel> samples) {
        this.samples = samples;
    }
}
