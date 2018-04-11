package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.NONE)
public class ItemContainer{
    @JsonProperty("content")
    @XmlElement(name="content")
    private List<ItemContent> content;

    public List<ItemContent> getContent() {
        return content;
    }

    public void setContent(List<ItemContent> content) {
        this.content = content;
    }
}