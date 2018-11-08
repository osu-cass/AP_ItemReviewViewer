package org.smarterbalanced.itemreviewviewer.web.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "itemrelease")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemRelease {

    @XmlElement (name = "item")
    @JsonProperty("Item")
    public ItemModel Itemmodel;
    public ItemModel getItem() {
        return Itemmodel;
    }

    public void setItem(ItemModel Item) {
        this.Itemmodel = Item;
    }
}
