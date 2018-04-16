package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.NONE)
public class ItemScoringModel {
    @XmlAttribute
    private String language;
    private String answerKey;
    private Boolean hasMachineRubric;
    @XmlElement(name = "optionlist")
    private List<ItemScoringOptionModel> scoringOptions;
    @XmlElement(name = "rubriclist")
    private List<RubricModel> rubrics;

    public ItemScoringModel(){

    }

    public ItemScoringModel(String answerKey, Boolean hasMachineRubric, List<ItemScoringOptionModel> scoringOptions, List<RubricModel> rubrics){
        this.answerKey = answerKey;
        this.hasMachineRubric = hasMachineRubric;
        this.scoringOptions = scoringOptions;
        this.rubrics = rubrics;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("answerKey")
    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    @JsonProperty("hasMachineRubric")
    public Boolean getHasMachineRubric() {
        return hasMachineRubric;
    }

    public void setHasMachineRubric(Boolean hasMachineRubric) {
        this.hasMachineRubric = hasMachineRubric;
    }

    @JsonProperty("scoringOptions")
    public List<ItemScoringOptionModel> getScoringOptions() {
        return scoringOptions;
    }

    public void setScoringOptions(List<ItemScoringOptionModel> scoringOptions) {
        this.scoringOptions = scoringOptions;
    }

    @JsonProperty("rubrics")
    public List<RubricModel> getRubrics() {
        return rubrics;
    }

    public void setRubrics(List<RubricModel> rubrics) {
        this.rubrics = rubrics;
    }
}
