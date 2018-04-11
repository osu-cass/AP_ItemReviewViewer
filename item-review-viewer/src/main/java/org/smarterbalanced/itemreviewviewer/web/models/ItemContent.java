package org.smarterbalanced.itemreviewviewer.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

public class ItemContent{

    private List<RubricModel> rubrics;

    public ItemContent(){

    }

    @XmlElement(name = "rubriclist")
    @JsonProperty("rubrics")
    public List<RubricModel> get_rubrics() {
        return rubrics;
    }

    public void set_rubrics(List<RubricModel> rubrics) {
        this.rubrics = rubrics;
    }
}
