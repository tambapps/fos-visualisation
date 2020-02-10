package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.FosData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Bubbles {

  public List<Bubble> toBubbles(Collection<ResearchPaper> researchPapers) {
    List<Bubble> bubbles = new ArrayList<>();
    Map<String, List<Float>> fosWeights = FosData.getAllFosWeights(researchPapers);
    Map<String, Integer> nbCitations = FosData.getAllFosCitations(researchPapers);


    return bubbles;
  }
}
