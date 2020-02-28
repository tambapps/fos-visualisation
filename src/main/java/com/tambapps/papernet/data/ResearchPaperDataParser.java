package com.tambapps.papernet.data;

import com.tambapps.papernet.configuration.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResearchPaperDataParser {

  private ResearchPaperDataParser() {
  }

  private static void addFosOccurenceEntry(Map<String, Integer> fosOccurenceMap, String line,
      int minOcc) {
    int commaIndex = line.lastIndexOf(',');
    String fos = line.substring(0, commaIndex).replaceAll("\"", "");
    int occ = Integer.parseInt(line.substring(commaIndex + 1));
    if (occ >= minOcc) {
      fosOccurenceMap.put(fos, occ);
    }
  }

  private static void addPaperInMaps(Map<Long, ResearchPaper> fosById,
      Map<Integer, List<ResearchPaper>> fosByYear, String line,
      Map<String, Integer> fosOccurenceMap) {
    String[] fields = FosParser.parseFields(line);
    ResearchPaper researchPaper = FosParser.parse(fields);
    // filter FOS that aren't in forOccurenceMap (too low occ)
    researchPaper.getFosWeights().entrySet()
        .removeIf(e -> !fosOccurenceMap.containsKey(e.getKey()));
    if (researchPaper.getFosWeights().isEmpty()) {
      return; // if all fos of this paper aren't in fosOccurenceMap, we ignore it
    }

    long id = FosParser.parseId(fields);
    fosByYear.computeIfAbsent(researchPaper.getYear(), k -> new ArrayList<>())
        .add(researchPaper);
    fosById.put(id, researchPaper);
  }

  public static ResearchPaperData parseData() throws IOException {
    return parseData(Integer.MAX_VALUE);
  }

  public static ResearchPaperData parseData(Integer maxArticles) throws IOException {
    Map<Long, ResearchPaper> fosById = new HashMap<>();
    Map<Integer, List<ResearchPaper>> fosByYear = new HashMap<>();
    Map<String, Integer> fosOccurenceMap = new HashMap<>();
    final int minFosOcc = Properties.getIntOrDefault("minFosOcc", 0);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        ResearchPaperDataParser.class.getResourceAsStream("/data/fos.csv")))) {
      reader.lines().skip(1)
          .forEach((line) -> addFosOccurenceEntry(fosOccurenceMap, line, minFosOcc));
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        ResearchPaperDataParser.class.getResourceAsStream("/data/dblp.v11.csv")))) {
      reader.lines()
          .skip(1)
          .limit(maxArticles)
          .forEach((line) -> addPaperInMaps(fosById, fosByYear, line, fosOccurenceMap));
    }
    return new ResearchPaperData(fosById, fosByYear, fosOccurenceMap);
  }
}
