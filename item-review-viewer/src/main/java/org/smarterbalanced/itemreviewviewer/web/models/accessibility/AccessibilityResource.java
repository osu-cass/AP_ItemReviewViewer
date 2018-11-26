package org.smarterbalanced.itemreviewviewer.web.models.accessibility;

import java.util.List;

/**
 * Created by noelcket on 11/26/2018.
 */
public class AccessibilityResource {
    private String _resourceCode;
    private String _currentSelection;
    private int _order;
    private String _DefaultSelection;
    private String _label;
    private String _description;
    private Boolean _disabled;
    private String _resourceTypeId;
    private String _infoTag;
    private List<AccessibilitySelection> _selections;


    public String get_resourceCode() {
        return _resourceCode;
    }

    public void set_resourceCode(String _resourceCode) {
        this._resourceCode = _resourceCode;
    }

    public String get_currentSelection() {
        return _currentSelection;
    }

    public void set_currentSelection(String _currentSelection) {
        this._currentSelection = _currentSelection;
    }

    public int get_order() {
        return _order;
    }

    public void set_order(int _order) {
        this._order = _order;
    }

    public String get_DefaultSelection() {
        return _DefaultSelection;
    }

    public void set_DefaultSelection(String _DefaultSelection) {
        this._DefaultSelection = _DefaultSelection;
    }

    public String get_label() {
        return _label;
    }

    public void set_label(String _label) {
        this._label = _label;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public Boolean get_disabled() {
        return _disabled;
    }

    public void set_disabled(Boolean _disabled) {
        this._disabled = _disabled;
    }

    public String getResourceTypeId() {
        return _resourceTypeId;
    }

    public void setResourceTypeId(String resourceTypeId) {
        _resourceTypeId = resourceTypeId;
    }

    public String getInfoTag() {
        return _infoTag;
    }

    public void setInfoTag(String infoTag) {
        this._infoTag = infoTag;
    }

    public List<AccessibilitySelection> get_selections() {
        return _selections;
    }

    public void set_selections(List<AccessibilitySelection> _selections) {
        this._selections = _selections;
    }

}
