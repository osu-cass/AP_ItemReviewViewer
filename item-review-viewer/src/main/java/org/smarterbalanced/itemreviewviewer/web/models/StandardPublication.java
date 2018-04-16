package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class StandardPublication {
    @XmlElement(name = "Publication")
    private String publication;
    @XmlElement(name = "PrimaryStandard")
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