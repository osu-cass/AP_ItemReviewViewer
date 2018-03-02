package org.smarterbalanced.itemreviewviewer.web.models.Accessibility;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AccessibilityResourceGroupModel {
    protected String label;
    protected int order;
    protected List<AccessibilityResourceModel> accessibilityResources;

    public AccessibilityResourceGroupModel(String label, int order, List<AccessibilityResourceModel> accessibilityResources){
        this.label = label;
        this.order = order;
        this.accessibilityResources = accessibilityResources;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("order")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @JsonProperty("accessibilityResource")
    public List<AccessibilityResourceModel> getAccessibilityResources() {
        return accessibilityResources;
    }

    public void setAccessibilityResources(List<AccessibilityResourceModel> accessibilityResources) {
        this.accessibilityResources = accessibilityResources;
    }
}
