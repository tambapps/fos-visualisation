package com.tambapps.papernet.visualisation.drawable;

import static com.tambapps.papernet.util.MathUtils.percentageMapping;
import static com.tambapps.papernet.util.MathUtils.toPercentage;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.WeightedCitation;
import com.tambapps.papernet.gl.pool.LinkPool;
import com.tambapps.papernet.gl.shader.Color;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FosNetUtils {

  public static final float MIN_RADIUS = 4;
  public static final float MAX_RADIUS = 50;
  public static final float MIN_LINK_WIDTH = 1;
  public static final float MAX_LINK_WIDTH = 22;
  private static Color START_COLOR = new Color(0, 0, 1);
  private static Color END_COLOR = new Color(1, 0, 0);

  private static float radiusScore(List<WeightedCitation> radiusScore) {
    return radiusScore.stream()
        .map(s -> 100 + s.getWeight() * 100f)
        .reduce(0f, Float::sum);
  }

  public static Map<String, Node> generate(Map<String, Node> cachedFosNodes,
                                           Collection<ResearchPaper> researchPapers, List<Link> links) {
    Map<String, List<WeightedCitation>> fosWeightedCitations =
        ResearchPaperData.getFosWeightedCitations(researchPapers);
    Map<String, Float> radiusScore = fosWeightedCitations.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> radiusScore(e.getValue())));
    Map<String, Float> citations = fosWeightedCitations.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            e -> (float) e.getValue().stream().mapToInt(WeightedCitation::getNbCitations).sum()));
    float minScore = Float.MAX_VALUE;
    float maxScore = Float.MIN_VALUE;
    for (Float score : radiusScore.values()) {
      if (maxScore < score) {
        maxScore = score;
      }
      if (minScore > score) {
        minScore = score;
      }
    }

    float minCitations = Float.MAX_VALUE;
    float maxCitations = Float.MIN_VALUE;
    for (Float citation : citations.values()) {
      if (maxCitations < citation) {
        maxCitations = citation;
      }
      if (minCitations > citation) {
        minCitations = citation;
      }
    }

    float finalMinScore = minScore;
    float finalMaxScore = maxScore;
    float finalMinCitations = minCitations;
    float finalMaxCitations = maxCitations;
    Map<String, NodeData> fosNodeData = fosWeightedCitations.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> FosNetUtils.toNodeData(
            radiusScore.get(e.getKey()), finalMinScore, finalMaxScore,
            citations.get(e.getKey()), finalMinCitations, finalMaxCitations,
            fosWeightedCitations.get(e.getKey()).size())));

    // links
    Map<String, Map<String, Integer>> connectedOccurenceMap = new HashMap<>();
    researchPapers.forEach(paper -> fillConnectionsMap(paper, connectedOccurenceMap));

    Map<String, Node> fosNode = fosNodeData.entrySet()
        .stream()
        .map(e -> getNode(cachedFosNodes, e.getKey(), e.getValue()))
        .collect(Collectors.toMap(Node::getFos, b -> b));

    float maxLinkOcc = connectedOccurenceMap.values()
        .stream()
        .flatMapToInt(m -> m.values().stream().mapToInt(i -> i))
        .max().orElse(0);
    float minLinkOcc = connectedOccurenceMap.values()
        .stream()
        .flatMapToInt(m -> m.values().stream().mapToInt(i -> i))
        .min().orElse(0);

    fillLinks(links, connectedOccurenceMap, fosNode, minLinkOcc, maxLinkOcc);
    return fosNode;
  }

  private static void fillLinks(List<Link> links,
                                Map<String, Map<String, Integer>> connectedOccurenceMap,
                                Map<String, Node> fosNode, float minLinkOcc, float maxLinkOcc) {
    for (String fos1 : connectedOccurenceMap.keySet()) {
      Map<String, Integer> occMap = connectedOccurenceMap.get(fos1);
      for (String fos2 : occMap.keySet()) {
        float nbOcc = occMap.get(fos2);
        float width =
            percentageMapping(nbOcc, minLinkOcc, maxLinkOcc, MIN_LINK_WIDTH, MAX_LINK_WIDTH);
        links.add(LinkPool.get(fosNode.get(fos1), fosNode.get(fos2), width));
      }
    }
  }

  private static void fillConnectionsMap(ResearchPaper paper,
      Map<String, Map<String, Integer>> connectedOccurenceMap) {
    for (String fos : paper.getFosWeights().keySet()) {
      for (String fos2 : paper.getFosWeights().keySet()) {
        if (fos.equals(fos2)) {
          continue;
        }
        String first = fos.compareTo(fos2) <= 0 ?
            fos :
            fos2; // order them in order to have all links for a couple of fos in one entry
        String second = first.equals(fos) ? fos2 : fos;
        Map<String, Integer> occMap =
            connectedOccurenceMap.computeIfAbsent(first, k -> new HashMap<>());
        int occ = occMap.getOrDefault(second, 0);
        occMap.put(second, occ + 1);
      }
    }
  }

  private static Node getNode(Map<String, Node> fosNodes, String fos, NodeData data) {
    Node b = fosNodes.get(fos);
    if (b != null) {
      b.getShader().setColor(data.r, data.g, data.b);
      b.setRadius(data.radius);
      b.setNCitations(data.citations);
      b.setNbOcc(data.nbOcc);
      return b;
    }
    try {
      return Node.newNode(fos, data.r, data.g, data.b, data.radius, data.nbOcc, data.citations);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't create node", e);
    }
  }

  private static NodeData toNodeData(float radiusScore, float minRadiusScore,
                                     float maxRadiusScore, float citations, float minCitations, float maxCitations, int nbOcc) {
    float radius =
        percentageMapping(radiusScore, minRadiusScore, maxRadiusScore, MIN_RADIUS, MAX_RADIUS);
    float citationPercentage = toPercentage(citations, minCitations, maxCitations);
    float r = getColorField(Color::getR, START_COLOR, END_COLOR, citationPercentage);
    float g = getColorField(Color::getG, START_COLOR, END_COLOR, citationPercentage);
    float b = getColorField(Color::getB, START_COLOR, END_COLOR, citationPercentage);
    return new NodeData(radius, r, g, b, (int) citations, nbOcc);
  }

  private static float getColorField(Function<Color, Float> fieldExtractor, Color start, Color end,
      float percentage) {
    return fieldExtractor.apply(start)
        + (fieldExtractor.apply(end) - fieldExtractor.apply(start))
        * percentage;
  }

  @AllArgsConstructor
  @Value
  private static class NodeData {
    private float radius;
    private float r;
    private float g;
    private float b;
    private int nbOcc;
    private int citations;
  }

}
