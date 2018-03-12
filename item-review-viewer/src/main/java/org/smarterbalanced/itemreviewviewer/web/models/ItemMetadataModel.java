package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarterbalanced.itemreviewviewer.web.models.AboutItemMetadataModel;

public class ItemMetadataModel {

    private String itemKey;
    private String bankKey;
    private String revision;
    private String section;
    private AboutItemMetadataModel aboutItemMetadataModel;
    private ItemScoringModel sampleItemScoring;

    public ItemMetadataModel(String itemKey, String bankKey, String revision, String section, AboutItemMetadataModel aboutItemMetadataModel, ItemScoringModel sampleItemScoring){
        this.itemKey = itemKey;
        this.bankKey = bankKey;
        this.revision = revision;
        this.section = section;
        this.aboutItemMetadataModel = aboutItemMetadataModel;
        this.sampleItemScoring = sampleItemScoring;
    }

    @JsonProperty("itemKey")
    public String getItemKey(){
        return itemKey;
    }

    @JsonProperty("bankKey")
    public String getBankKey(){
        return bankKey;
    }

    @JsonProperty("revision")
    public String getRevision(){
        return revision;
    }

    @JsonProperty("section")
    public String getSection(){
        return section;
    }

    @JsonProperty("AboutItemMetadata")
    public AboutItemMetadataModel getAboutItemMetadata(){
        return aboutItemMetadataModel;
    }

    @JsonProperty("sampleItemScoring")
    public ItemScoringModel getSampleItemScoring() {
        return sampleItemScoring;
    }
}
