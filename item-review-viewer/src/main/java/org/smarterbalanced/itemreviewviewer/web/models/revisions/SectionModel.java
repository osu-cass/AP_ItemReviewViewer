package org.smarterbalanced.itemreviewviewer.web.models.revisions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SectionModel {
    public String key;
    public String value;

    public SectionModel(String key, String value){
        this.key = key;
        this.value = value;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }
}
