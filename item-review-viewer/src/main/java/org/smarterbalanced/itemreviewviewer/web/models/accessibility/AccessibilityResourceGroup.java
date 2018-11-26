package org.smarterbalanced.itemreviewviewer.web.models.accessibility;

import java.util.List;

/**
 * Created by noelcket on 11/26/2018.
 */
public class AccessibilityResourceGroup {
    private String _label;
    private int _order;
    private List<AccessibilityResource> _accessibilityResources;

    public AccessibilityResourceGroup() {

    }

    public AccessibilityResourceGroup(String _label, int _order, List<AccessibilityResource> _accessibilityResources) {
        this._label = _label;
        this._order = _order;
        this._accessibilityResources = _accessibilityResources;
    }

    public String get_label() {
        return _label;
    }

    public void set_label(String _label) {
        this._label = _label;
    }

    public int get_order() {
        return _order;
    }

    public void set_order(int order) {
        _order = order;
    }

    public List<AccessibilityResource> getAccessibilityResources() {
        return _accessibilityResources;
    }

    public void setAccessibilityResources(List<AccessibilityResource> accessibilityResources) {
        this._accessibilityResources = accessibilityResources;
    }
}
