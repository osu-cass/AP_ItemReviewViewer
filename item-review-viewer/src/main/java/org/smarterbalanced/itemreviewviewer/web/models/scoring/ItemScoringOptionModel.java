package org.smarterbalanced.itemreviewviewer.web.models.scoring;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.List;


public class ItemScoringOptionModel {
    @XmlAttribute(name = "minChoices")
    @JsonProperty("minChoices")
    public String minChoices;

    @XmlElement(name = "maxChoices")
    @JsonProperty("maxChoices")
    public String maxChoices;

    @XmlElement(name="option")
    @JsonProperty("scoringOptions")
    public List<ScoringOptionModel> options;


}
