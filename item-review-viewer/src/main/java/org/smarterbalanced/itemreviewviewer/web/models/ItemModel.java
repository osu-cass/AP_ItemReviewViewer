package org.smarterbalanced.itemreviewviewer.web.models;

public class ItemModel {
    private String namespace;
    private String bankKey;
    private String ItemKey;
    private boolean isExists;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getBankKey() {
        return bankKey;
    }

    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    public String getItemKey() {
        return ItemKey;
    }

    public void setItemKey(String itemKey) {
        ItemKey = itemKey;
    }

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "namespace='" + namespace + '\'' +
                ", bankKey='" + bankKey + '\'' +
                ", ItemKey='" + ItemKey + '\'' +
                ", isExists=" + isExists +
                '}';
    }
}
