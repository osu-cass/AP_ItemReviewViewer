package org.smarterbalanced.itemreviewviewer.web.models.scoring;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "optionlist")
@XmlAccessorType(XmlAccessType.NONE)
public class ScoringOptionModel {
    @XmlElement(name = "name")
    @JsonProperty("name")
    private String name;

    @XmlElement(name = "val")
    @JsonProperty("value")
    private String val;

    @XmlElement(name = "feedback")
    @JsonProperty("feedback")
    private String feedback;

    @JsonProperty("language")
    private String language;

    @JsonProperty("answer")
    private String answer;

    public ScoringOptionModel(){

    }

    public ScoringOptionModel(String name, String value, String feedback, String language, String answer){
        this.name = name;
        this.val = value;
        this.feedback = feedback;
        this.language = language;
        this.answer = answer;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return val;
    }

    public void setValue(String val) {
        this.val = val;
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
