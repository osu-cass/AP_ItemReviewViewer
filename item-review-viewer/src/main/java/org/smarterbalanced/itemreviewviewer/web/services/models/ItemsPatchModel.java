package org.smarterbalanced.itemreviewviewer.web.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
@XmlRootElement (name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"row"})
public class ItemsPatchModel {
    @XmlElement(name = "row")
    private ItemsPatchRow[] row;
    @JsonProperty("row")
    public ItemsPatchRow[] getRow() {
        return row;
    }

    public void setRow(ItemsPatchRow[] row) {
        this.row = row;
    }
}
