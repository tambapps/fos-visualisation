package com.tambapps.papernet.data;

import com.tambapps.papernet.configuration.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FosDataParser {

  private FosDataParser() {}

  private static void addFosOccurenceEntry(Map<String, Integer> fosOccurenceMap, String line) {
    int commaIndex = line.lastIndexOf(',');
    String fos = line.substring(0, commaIndex).replaceAll("\"", "");
    fosOccurenceMap.put(fos, Integer.parseInt(line.substring(commaIndex + 1)));
  }

  private static void addFosInMaps(Map<Integer, Fos> fosById, Map<Integer, Fos> fosByYear, String line) {
    String[] fields = FosParser.parseFields(line);
    Fos fos = FosParser.parse(fields);
    int id = FosParser.parseId(fields);
    fosByYear.put(fos.getYear(), fos);
    fosById.put(id, fos);
  }

  private static int parseId(String line) {
    return Integer.parseInt(line.split(",")[0].replaceAll("\"", ""));
  }

  public static FosData parseData() throws IOException {
    Map<Integer, Fos> fosById = new HashMap<>();
    Map<Integer, Fos> fosByYear = new HashMap<>();
    Map<String, Integer> fosOccurenceMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(FosDataParser.class.getResourceAsStream("/data/fos.csv")))) {
      reader.lines().skip(1).forEach((line) -> addFosOccurenceEntry(fosOccurenceMap, line));
    }
    int maxFos = Optional.ofNullable(Properties.getInt("maxFos")).orElse(Integer.MAX_VALUE);

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(FosDataParser.class.getResourceAsStream("/data/dblp.v11.csv")))) {
      reader.lines()
        .skip(1)
        .limit(maxFos)
        .forEach((line) -> addFosInMaps(fosById, fosByYear, line));
    }

    return new FosData(fosById, fosByYear, fosOccurenceMap);
  }
}
