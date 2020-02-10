package com.tambapps.papernet.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FosData {

  private final Map<Integer, ResearchPaper> fosById;
  private final Map<Integer, ResearchPaper> fosByYear;
  private final Map<String, Integer> fosOccurenceMap;

  public static Map<String, List<Float>> getAllFosWeights(Collection<ResearchPaper> foses) {
    Map<String, List<Float>> fosWeights = new HashMap<>();
    foses.stream()
      .map(ResearchPaper::getFosWeights)
      .flatMap(map -> map.entrySet().stream())
      .forEach(entry -> {
        List<Float> weights = fosWeights.computeIfAbsent(entry.getKey(),
          k -> new ArrayList<>());
        weights.add(entry.getValue());
      });
    return fosWeights;
  }

  public static Map<String, Integer> getAllFosCitations(Collection<ResearchPaper> researchPapers) {
    Map<String, Integer> fosCitations = new HashMap<>();
    researchPapers.forEach(f -> {
      for (String fos : f.getFosWeights().keySet()) {
        fosCitations.put(fos, f.getNCitations());
      }
    });
    return fosCitations;
  }
}
