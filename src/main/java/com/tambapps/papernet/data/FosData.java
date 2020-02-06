package com.tambapps.papernet.data;

import lombok.Data;

import java.util.Map;

@Data
public class FosData {

  private final Map<String, Integer> fosOccurenceMap;

  public FosData(Map<String, Integer> fosOccurenceMap) {
    this.fosOccurenceMap = fosOccurenceMap;
  }
}
