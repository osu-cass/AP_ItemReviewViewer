package org.smarterbalanced.itemreviewviewer.web.models.Accessibility;

import com.fasterxml.jackson.annotation.JsonProperty;

        import java.util.List;

public class AccessibilityResourceModel {
    @JsonProperty("disabled")
    protected boolean disabled;
    protected String resourceCode;
    protected String defaultSelection;
    protected String description;
    protected String label;
    protected String currentSelectionCode;
    protected int order;
    protected List<DropDownSelectionModel> selections;

    public AccessibilityResourceModel(boolean disabled, String resourceCode, String defaultSelection, String description, String label, String currentSelectionCode, int order, List<DropDownSelectionModel> selections){
        this.disabled = disabled;
        this.resourceCode = resourceCode;
        this.defaultSelection = defaultSelection;
        this.description = description;
        this.label = label;
        this.currentSelectionCode = currentSelectionCode;
        this.order = order;
        this.selections = selections;
    }

    @JsonProperty("resourceCode")
    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    @JsonProperty("defaultSelection")
    public String getDefaultSelection() {
        return defaultSelection;
    }

    public void setDefaultSelection(String defaultSelection) {
        this.defaultSelection = defaultSelection;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("currentSelectionCode")
    public String getCurrentSelectionCode() {
        return currentSelectionCode;
    }

    public void setCurrentSelectionCode(String currentSelectionCode) {
        this.currentSelectionCode = currentSelectionCode;
    }

    @JsonProperty("order")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @JsonProperty("selections")
    public List<DropDownSelectionModel> getSelections() {
        return selections;
    }

    public void setSelections(List<DropDownSelectionModel> selections) {
        this.selections = selections;
    }
}
