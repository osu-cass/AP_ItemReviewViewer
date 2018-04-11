package org.smarterbalanced.itemreviewviewer.web.models;

import AIR.Common.Helpers.CaseInsensitiveMap;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="itemrelease")
@XmlAccessorType(XmlAccessType.NONE)
public class ItemDocument{
    @JsonProperty("item")
    @XmlElement(name="item")
    public ItemContainer item;

    public ItemContainer getItem() {
        return item;
    }

    public void setItem(ItemContainer item) {
        this.item = item;
    }
}
