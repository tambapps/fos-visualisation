package com.tambapps.papernet.data;

import lombok.Data;

import java.util.Map;

@Data
public class Fos {

  // id stored in map from FosData
  private final Map<String, Float> fosWeights;
  private final int year;
  private final int nCitations;

}
