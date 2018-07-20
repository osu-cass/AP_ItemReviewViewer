package org.smarterbalanced.itemreviewviewer.web.services.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Namespace {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    public Boolean hasBankKey;

    public int bankKey;

    public Namespace() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasBankKey() {
        return hasBankKey;
    }

    public void setHasBankKey(Boolean hasBankKey) {
        this.hasBankKey = hasBankKey;
    }

    public int getBankKey() {
        return bankKey;
    }

    public void setBankKey(int bankKey) {
        this.bankKey = bankKey;
    }
}
