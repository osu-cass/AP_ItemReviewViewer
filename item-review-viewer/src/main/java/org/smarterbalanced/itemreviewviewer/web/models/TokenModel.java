//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import tds.irisshared.models.AccommodationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TokenModel {
    @JsonIgnore
    private List<HashMap<String, String>> items = new ArrayList();
    private List<AccommodationModel> accommodations;
    private String loadFrom;
    private String revision;
    private String section;

    public TokenModel(String[] itemIds, List<AccommodationModel> accommodations, String revision, String section, String loadFrom) {
        String[] arr$ = itemIds;
        int len$ = itemIds.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String item = arr$[i$];
            HashMap<String, String> itemhash = new HashMap();
            itemhash.put("id", "i-" + item);
            itemhash.put("response", "");
            this.items.add(itemhash);
        }

        this.accommodations = accommodations;
        this.loadFrom = loadFrom;
        this.revision = revision;
        this.section = section;
    }

    @JsonProperty("items")
    public List<HashMap<String, String>> getItems() {
        return this.items;
    }

    @JsonProperty("accommodations")
    public List<AccommodationModel> getAccommodations() {
        return this.accommodations;
    }

    @JsonProperty("loadFrom")
    public String getLoadFrom() {
        return this.loadFrom;
    }

    @JsonProperty("revision")
    public String getRevision() { return this.revision; }

    @JsonProperty("section")
    public String getSection() { return this.section; }
}
