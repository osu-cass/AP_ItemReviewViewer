package org.smarterbalanced.itemreviewviewer.web.models.accessibility;

/**
 * Created by noelcket on 11/26/2018.
 */
public class AccessibilitySelection {
    private String _selectionCode;
    private String _label;
    private int _order;
    private Boolean _disabled;

    public AccessibilitySelection(){

    }

    public String get_selectionCode() {
        return _selectionCode;
    }

    public void set_selectionCode(String _selectionCode) {
        this._selectionCode = _selectionCode;
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

    public void set_order(int _order) {
        this._order = _order;
    }

    public Boolean get_disabled() {
        return _disabled;
    }

    public void set_disabled(Boolean _disabled) {
        this._disabled = _disabled;
    }

    public Boolean get_hidden() {
        return _hidden;
    }

    public void set_hidden(Boolean _hidden) {
        this._hidden = _hidden;
    }

    private Boolean _hidden;

    public AccessibilitySelection(String _selectionCode, String _label, int _order, Boolean _disabled, Boolean _hidden) {
        this._selectionCode = _selectionCode;
        this._label = _label;
        this._order = _order;
        this._disabled = _disabled;
        this._hidden = _hidden;
    }

}
