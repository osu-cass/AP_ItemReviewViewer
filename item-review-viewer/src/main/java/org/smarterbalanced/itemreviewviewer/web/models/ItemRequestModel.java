//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tds.irisshared.models.AccommodationModel;
import tds.irisshared.models.AccommodationTypeLookup;

public class ItemRequestModel {
    private final String[] items;
    private final ArrayList<String> featureCodes;
    private final String section;
    private final String revision;
    private List<AccommodationModel> accommodations;
    private final String loadFrom;
    private static final Logger logger = LoggerFactory.getLogger(ItemRequestModel.class);

    public ItemRequestModel(String[] items, ArrayList<String> featureCodes, String loadFrom) {
        this.items = items;
        this.featureCodes = featureCodes;
        this.accommodations = new ArrayList();
        this.loadFrom = loadFrom;
        this.revision = null;
        this.section = null;
    }

    public ItemRequestModel(String[] items, ArrayList<String> featureCodes, String section, String revision, String loadFrom) {
        this.items = items;
        this.featureCodes = featureCodes;
        this.accommodations = new ArrayList();
        this.loadFrom = loadFrom;
        this.revision = revision;
        this.section = section;
    }

    private void buildAccommodations() {
        HashMap<String, List<String>> accomms = new HashMap();
        Iterator i$ = this.featureCodes.iterator();

        String type;
        List accomCodes;
        while(i$.hasNext()) {
            String code = (String)i$.next();
            if (!StringUtils.isEmpty(code)) {
                type = AccommodationTypeLookup.getType(code);
                if (type != null) {
                    if (accomms.containsKey(type)) {
                        accomCodes = (List)accomms.get(type);
                        accomCodes.add(code);
                        accomms.put(type, accomCodes);
                    } else {
                        accomCodes = new ArrayList<>();
                        accomCodes.add(code);
                        accomms.put(type, accomCodes);
                    }
                } else {
                    logger.info("Unknown accommodation code requested for item " + this.items[0] + " code: " + code);
                }
            }
        }

        i$ = accomms.entrySet().iterator();

        while(i$.hasNext()) {
            Entry<String, List<String>> entry = (Entry)i$.next();
            type = (String)entry.getKey();
            accomCodes = (List)entry.getValue();
            AccommodationModel accommodation = new AccommodationModel(type, accomCodes);
            this.accommodations.add(accommodation);
        }

    }

    private void checkConditionalIsaapCodes() {
        Iterator i$ = AccommodationTypeLookup.getCodes("TTS").iterator();

        while(i$.hasNext()) {
            String code = (String)i$.next();
            if (this.featureCodes.contains(code)) {
                i$ = AccommodationTypeLookup.getConditionalTypes().entrySet().iterator();

                while(i$.hasNext()) {
                    Entry<String, String> entry = (Entry)i$.next();
                    if (!this.featureCodes.contains(entry.getKey())) {
                        String conditionalCode = (String)entry.getKey();
                        this.featureCodes.add(conditionalCode);
                    }
                }

                return;
            }
        }

    }

    private void checkDefaultIsaapCodes() {
        Iterator i$ = AccommodationTypeLookup.getDefaultTypes().entrySet().iterator();

        while(i$.hasNext()) {
            Entry<String, String> entry = (Entry)i$.next();
            if (!this.featureCodes.contains(entry.getKey())) {
                String defaultCode = (String)entry.getKey();
                this.featureCodes.add(defaultCode);
            }
        }

    }

    public String generateJsonToken() {
        ObjectMapper mapper = new ObjectMapper();
        this.checkDefaultIsaapCodes();
        this.checkConditionalIsaapCodes();
        this.buildAccommodations();
        TokenModel token = new TokenModel(this.items, this.accommodations, this.revision, this.section, this.loadFrom);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            String json = mapper.writer().writeValueAsString(token);
            return json;
        } catch (Exception var5) {
            logger.error(var5.getMessage());
            return "";
        }
    }
}
