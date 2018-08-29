package org.smarterbalanced.itemreviewviewer.web.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ItemsPatchRow {
    @XmlElement(name = "ItemId")
    private String itemId;
    @XmlElement (name = "Claim")
    private String claim;

    @JsonProperty("claim")
    public String getClaim() {
        return claim;
    }
    @JsonProperty("itemId")
    public String getItemId() {
        return itemId;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
