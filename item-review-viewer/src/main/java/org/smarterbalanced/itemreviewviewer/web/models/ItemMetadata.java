package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarterbalanced.itemreviewviewer.web.models.AboutItemMetadata;

public class ItemMetadata {

    private String itemKey;
    private String bankKey;
    private String revision;
    private String section;
    private AboutItemMetadata aboutItemMetadata;

    public ItemMetadata(String itemKey, String bankKey, String revision, String section){
        this.itemKey = itemKey;
        this.bankKey = bankKey;
        this.revision = revision;
        this.section = section;
        this.aboutItemMetadata = new AboutItemMetadata(itemKey, bankKey, revision, section);
    }

    @JsonProperty("AboutItemMetadata")
    public AboutItemMetadata getAboutItemMetadata(){
        return aboutItemMetadata;
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
}
