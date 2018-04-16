package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class ItemContainer{
    @JsonProperty("content")
    public List<ItemScoringModel> content;
}