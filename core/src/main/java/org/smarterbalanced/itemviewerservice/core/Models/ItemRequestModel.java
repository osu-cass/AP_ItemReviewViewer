package org.smarterbalanced.itemviewerservice.core.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemviewerservice.dal.Config.SettingsReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models an item request.
 */
public class ItemRequestModel {

  private final String item;
  private final String[] featureCodes;
  private List<AccommodationModel> accommodations;
  private static final Logger logger = LoggerFactory.getLogger(ItemRequestModel.class);

  /**
   * Instantiates a new Item model.
   *
   * @param item         The item requested
   * @param featureCodes Accessibility feature codes
   */
  public ItemRequestModel(String item, String[] featureCodes) {
    this.item = item;
    this.featureCodes = featureCodes;
    this.accommodations = new ArrayList<>();
  }

  private Boolean itemExists() {
    String irisContentPath;
    try {
      irisContentPath = SettingsReader.readIrisContentPath();
    } catch (Exception e) {
      logger.warn("Unable to load iris content path");
      return false;
    }
    File item = new File(irisContentPath + "/" + "Items" );
    return false;
  }

  private void buildAccommodations() {
    HashMap<String, List<String>> accomms = new HashMap<>();
    for (String code: this.featureCodes) {
      String type = AccommodationTypeLookup.getType(code);
      //If type is null then the accommodation is not found. Do not add it to the list.
      if (type != null) {
        if (accomms.containsKey(type)) {
          List<String> accomCodes = accomms.get(type);
          accomCodes.add(code);
          accomms.put(type, accomCodes);
        } else {
          List<String> accomCodes = new ArrayList<String>();
          accomCodes.add(code);
          accomms.put(type, accomCodes);
        }
      } else {
        logger.info("Unknown accommodation code requested for item " + this.item + " code: "
                + code);
      }
    }
    for (Map.Entry<String, List<String>> entry: accomms.entrySet()) {
      String type = entry.getKey();
      List<String> codes = entry.getValue();
      AccommodationModel accommodation = new AccommodationModel(type, codes);
      this.accommodations.add(accommodation);
    }
  }

  /**
   * Generate a json representation of the requested item and accommodations.
   * The token is in the format taken by the blackbox javascript.
   *
   * @return the Json token as a string
   */
  public String generateJsonToken() {
    ObjectMapper mapper = new ObjectMapper();
    buildAccommodations();
    String json;
    TokenModel token = new TokenModel(this.item, this.accommodations);
    try {
      json = mapper.writer().writeValueAsString(token);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return "";
    }
    return json;
  }
}

