package com.tambapps.papernet.data;

import com.tambapps.papernet.configuration.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ResearchPaperDataParser {

  private ResearchPaperDataParser() {}

  private static void addFosOccurenceEntry(Map<String, Integer> fosOccurenceMap, String line) {
    int commaIndex = line.lastIndexOf(',');
    String fos = line.substring(0, commaIndex).replaceAll("\"", "");
    fosOccurenceMap.put(fos, Integer.parseInt(line.substring(commaIndex + 1)));
  }

  private static void addPaperInMaps(Map<Integer, ResearchPaper> fosById, Map<Integer, List<ResearchPaper>> fosByYear, String line) {
    String[] fields = FosParser.parseFields(line);
    ResearchPaper researchPaper = FosParser.parse(fields);
    int id = FosParser.parseId(fields);
    fosByYear.computeIfAbsent(researchPaper.getYear(), k -> new ArrayList<>())
      .add(researchPaper);
    fosById.put(id, researchPaper);
  }

  public static ResearchPaperData parseData() throws IOException {
    return parseData(Integer.MAX_VALUE);
  }

  public static ResearchPaperData parseData(Integer maxArticles) throws IOException {
    Map<Integer, ResearchPaper> fosById = new HashMap<>();
    Map<Integer, List<ResearchPaper>> fosByYear = new HashMap<>();
    Map<String, Integer> fosOccurenceMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(ResearchPaperDataParser.class.getResourceAsStream("/data/fos.csv")))) {
      reader.lines().skip(1).forEach((line) -> addFosOccurenceEntry(fosOccurenceMap, line));
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(ResearchPaperDataParser.class.getResourceAsStream("/data/dblp.v11.csv")))) {
      reader.lines()
        .skip(1)
        .limit(maxArticles)
        .forEach((line) -> addPaperInMaps(fosById, fosByYear, line));
    }
    return new ResearchPaperData(fosById, fosByYear, fosOccurenceMap);
  }
}
