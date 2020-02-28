package com.tambapps.papernet.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResearchPaperData {

  private final Map<Long, ResearchPaper> paperById;
  private final Map<Integer, List<ResearchPaper>> papersByYear;
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

  public static Map<String, List<WeightedCitation>> getFosWeightedCitations(
      Collection<ResearchPaper> researchPapers) {
    Map<String, List<WeightedCitation>> fosWeightedCitations = new HashMap<>();
    for (ResearchPaper paper : researchPapers) {
      int nbCitation = paper.getNCitations();
      for (Map.Entry<String, Float> entry : paper.getFosWeights().entrySet()) {
        List<WeightedCitation> weightedCitations =
            fosWeightedCitations.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
        weightedCitations.add(new WeightedCitation(nbCitation, entry.getValue()));
      }
    }
    return fosWeightedCitations;
  }

  public Collection<ResearchPaper> getAllPapers() {
    return paperById.values();
  }

  public List<ResearchPaper> getAllByYear(int year) {
    return papersByYear.getOrDefault(year, List.of());
  }
}
