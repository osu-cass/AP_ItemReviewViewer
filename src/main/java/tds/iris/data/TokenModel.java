package tds.iris.data;

/**
 * @author ssuryadevara
 *
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import tds.blackbox.ContentRequestAccommodation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type that models the Json token passed to the JavaScript frontend.
 */
public class TokenModel {
  @JsonIgnore
  private List<HashMap<String, String>> items;
  private List<ContentRequestAccommodation> accommodations;

  /**
   * Instantiates a new Token model.
   *
   * @param item           the item
   * @param accommodations the accommodations for the item
   */
  public TokenModel(String item, List<ContentRequestAccommodation> accommodations) {
    this.items = new ArrayList<>();
    HashMap<String, String> itemhash = new HashMap<>();
    itemhash.put("id", item);
    itemhash.put("response", "");
    this.items.add(itemhash);
    this.accommodations = accommodations;
  }

  /**
   * Gets the items in the token.
   *
   * @return the items
   */
  @JsonProperty("items")
  public List<HashMap<String, String>> getItems() {
    return this.items;
  }

  /**
   * Gets the accommodations in the token.
   *
   * @return the accommodations
   */
  @JsonProperty("accommodations")
  public List<ContentRequestAccommodation> getAccommodations() {
    return this.accommodations;
  }
}
