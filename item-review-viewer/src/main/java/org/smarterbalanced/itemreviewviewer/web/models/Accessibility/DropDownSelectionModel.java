package org.smarterbalanced.itemreviewviewer.web.models.Accessibility;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropDownSelectionModel {

    @JsonProperty("disabled")
    public boolean disabled;
    @JsonProperty("hidden")
    public boolean hidden;
    protected String label;
    protected String selectionCode;
    protected int order;

    public DropDownSelectionModel(boolean disabled, boolean hidden, String label, String selectionCode, int order){
        this.disabled = disabled;
        this.hidden = hidden;
        this.label = label;
        this.selectionCode = selectionCode;
        this.order = order;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("selectionCode")
    public String getSelectionCode() {
        return selectionCode;
    }

    public void setSelectionCode(String selectionCode) {
        this.selectionCode = selectionCode;
    }

    @JsonProperty("order")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
