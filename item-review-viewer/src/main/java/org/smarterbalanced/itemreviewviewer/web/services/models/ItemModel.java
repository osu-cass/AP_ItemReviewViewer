package org.smarterbalanced.itemreviewviewer.web.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarterbalanced.itemreviewviewer.web.services.models.AttribList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "item")
@XmlAccessorType(XmlAccessType.NONE)
public class ItemModel {
    private String associatedPassage;
    private AttribList attribList;
    private String tutorial;



    @XmlElement(name = "associatedpassage")
    @JsonProperty("associatedPassage")
    public String getAssociatedPassage (){
        return associatedPassage;
    }
    private void setAsscociatedPassage (String associatedPassage) {
        this.associatedPassage = associatedPassage;
    }

    @XmlElement (name = "attriblist")
    @JsonProperty("attribList")
    public AttribList getAttribList () {
        return attribList;
    }

    public void setAttribList(AttribList attribList) {
        this.attribList = attribList;
    }
    @XmlElement (name = "tutorial")
    @JsonProperty("Tutorial")
    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }


}
