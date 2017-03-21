package org.smarterbalanced.irv.core.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AccommodationModel {
  private String type;
  private List<String> codes;

  public AccommodationModel(String type, List<String> codes) {
    this.type = type;
    this.codes = codes;
  }

  @JsonProperty("type")
  public String getType() {
    return this.type;
  }

  @JsonProperty("codes")
  public List<String> getCodes() {
    return this.codes;
  }

}
