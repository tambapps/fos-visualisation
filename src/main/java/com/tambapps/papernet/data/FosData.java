package com.tambapps.papernet.data;

import lombok.Data;

import java.util.Map;

@Data
public class FosData {

  private final Map<Integer, Fos> fosById;
  private final Map<Integer, Fos> fosByYear;
  private final Map<String, Integer> fosOccurenceMap;

}
