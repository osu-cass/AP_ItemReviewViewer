package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StandardPublication {

    private String publication;
    private String primaryStandard;

    @JsonProperty("Publication")
    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    @JsonProperty("PrimaryStandard")
    public String getPrimaryStandard() {
        return primaryStandard;
    }

    public void setPrimaryStandard(String primaryStandard) {
        this.primaryStandard = primaryStandard;
    }

}