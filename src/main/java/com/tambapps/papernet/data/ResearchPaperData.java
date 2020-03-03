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
