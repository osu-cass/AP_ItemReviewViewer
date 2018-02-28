package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class RubricList {

    protected List<Rubric> rubrics;

    public void addRubric(String name, String val, String scorepoint){
        Rubric rubric = new Rubric(name, val, scorepoint);
        if(this.rubrics == null){
            this.rubrics = new ArrayList<Rubric>();
        }
        this.rubrics.add(rubric);
    }

    @JsonProperty("rubrics")
    public List<Rubric> getRubrics() {
        if (rubrics == null) {
            rubrics = new ArrayList<Rubric>();
        }
        return this.rubrics;
    }

}


