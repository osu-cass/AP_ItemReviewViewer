package org.smarterbalanced.itemreviewviewer.web.models.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoringModel;

public class ItemMetadataModel {

    private String namespace;
    private String itemKey;
    private String bankKey;
    private String revision;
    private String section;
    private AboutItemMetadataModel aboutItemMetadataModel;
    private ItemScoringModel sampleItemScoring;

    public ItemMetadataModel(){

    }

    public ItemMetadataModel(String namespace, String itemKey, String bankKey, String revision, String section,
                             AboutItemMetadataModel aboutItemMetadataModel, ItemScoringModel sampleItemScoring){
        this.namespace = namespace;
        this.itemKey = itemKey;
        this.bankKey = bankKey;
        this.revision = revision;
        this.section = section;
        this.aboutItemMetadataModel = aboutItemMetadataModel;
        this.sampleItemScoring = sampleItemScoring;
    }

    @JsonProperty("namespace")
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @JsonProperty("itemKey")
    public String getItemKey(){
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @JsonProperty("bankKey")
    public String getBankKey(){
        return bankKey;
    }

    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    @JsonProperty("revision")
    public String getRevision(){
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @JsonProperty("section")
    public String getSection(){
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @JsonProperty("AboutItemMetadata")
    public AboutItemMetadataModel getAboutItemMetadata(){
        return aboutItemMetadataModel;
    }

    public void setAboutItemMetadataModel(AboutItemMetadataModel aboutItemMetadataModel) {
        this.aboutItemMetadataModel = aboutItemMetadataModel;
    }

    @JsonProperty("sampleItemScoring")
    public ItemScoringModel getSampleItemScoring() {
        return sampleItemScoring;
    }

    public void setSampleItemScoring(ItemScoringModel sampleItemScoring) {
        this.sampleItemScoring = sampleItemScoring;
    }
}
