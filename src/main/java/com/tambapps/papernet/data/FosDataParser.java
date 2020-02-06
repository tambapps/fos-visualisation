package com.tambapps.papernet.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class FosDataParser {

  private FosDataParser() {}

  private static void addFosOccurenceEntry(Map<String, Integer> fosOccurenceMap, String line) {
    int commaIndex = line.lastIndexOf(',');
    String fos = line.substring(0, commaIndex).replaceAll("\\\"", "");
    fosOccurenceMap.put(fos, Integer.parseInt(line.substring(commaIndex + 1)));
  }

  public static FosData parseData() throws IOException {

    Map<String, Integer> fosOccurenceMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(FosDataParser.class.getResourceAsStream("/data/fos.csv")))) {
      reader.lines().skip(1).forEach((line) -> addFosOccurenceEntry(fosOccurenceMap, line));
    }


    return new FosData(fosOccurenceMap);
  }
}
