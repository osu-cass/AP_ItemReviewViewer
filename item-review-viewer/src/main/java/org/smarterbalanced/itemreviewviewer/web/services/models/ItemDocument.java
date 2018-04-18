package org.smarterbalanced.itemreviewviewer.web.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="itemrelease")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class ItemDocument{
    @JsonProperty("item")
    public ItemContainer item;
}
