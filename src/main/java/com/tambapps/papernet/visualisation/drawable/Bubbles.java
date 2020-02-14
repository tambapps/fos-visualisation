package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.WeightedCitation;
import com.tambapps.papernet.gl.shader.Color;
import com.tambapps.papernet.gl.shader.ShaderUtils;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Bubbles {

  private static Color START_COLOR = new Color(0, 0, 1);
  private static Color END_COLOR = new Color(1, 0, 0);

  public static final float MIN_RADIUS = 4;
  public static final float MAX_RADIUS = 50;

  public static final float MIN_LINK_WIDTH = 1;
  public static final float MAX_LINK_WIDTH = 22;

  private static float radiusScore(List<WeightedCitation> radiusScore) {
    return radiusScore.stream()
      .map(s -> 100 + s.getWeight() * 100f)
      .reduce(0f, Float::sum);
  }

  public static Map<String, Bubble> toBubbles(Map<String, Bubble> fosBubbles, Collection<ResearchPaper> researchPapers, List<Link> links) {
    Map<String, List<WeightedCitation>> fosWeightedCitations = ResearchPaperData.getFosWeightedCitations(researchPapers);
    Map<String, Float> radiusScore = fosWeightedCitations.entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> radiusScore(e.getValue())));
    Map<String, Float> citations = fosWeightedCitations.entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey,
        e -> (float)e.getValue().stream().mapToInt(WeightedCitation::getNbCitations).sum()));
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
    Map<String, BubbleData> fosBubbleData = fosWeightedCitations.entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> Bubbles.toBubbleData(
        radiusScore.get(e.getKey()), finalMinScore, finalMaxScore,
        citations.get(e.getKey()), finalMinCitations, finalMaxCitations)));

    // links
    Map<String, Map<String, Integer>> connectedOccurenceMap = new HashMap<>();
    researchPapers.forEach(paper -> fillConnectionsMap(paper, connectedOccurenceMap));

    Map<String, Bubble> fosBubble = fosBubbleData.entrySet()
      .stream()
      .map(e -> createBubble(fosBubbles, e.getKey(), e.getValue()))
      .collect(Collectors.toMap(Bubble::getText, b -> b));

    float maxLinkOcc = connectedOccurenceMap.values()
      .stream()
      .flatMapToInt(m -> m.values().stream().mapToInt(i -> i))
      .max().getAsInt();
    float minLinkOcc = connectedOccurenceMap.values()
      .stream()
      .flatMapToInt(m -> m.values().stream().mapToInt(i -> i))
      .min().getAsInt();

    links.clear(); // TODO create link pool
    fillLinks(links, connectedOccurenceMap, fosBubble, minLinkOcc, maxLinkOcc);
    return fosBubble;
   // fosBubble.values()
     // .stream()
      //.sorted(Comparator.comparing(Bubble::getRadius).reversed()) // in decroissant order to draw big bubbles first
      //.collect(Collectors.toMap(Bubble::getText, b -> b));
  }

  private static void fillLinks(List<Link> links, Map<String, Map<String, Integer>> connectedOccurenceMap,
                                Map<String, Bubble> fosBubble, float minLinkOcc, float maxLinkOcc) {
    for (String fos1 : connectedOccurenceMap.keySet()) {
      Map<String, Integer> occMap = connectedOccurenceMap.get(fos1);
      for (String fos2 : occMap.keySet()) {
        float nbOcc = occMap.get(fos2);
        float width = MIN_LINK_WIDTH + toPercentage(nbOcc, minLinkOcc, maxLinkOcc) * (MAX_LINK_WIDTH - MIN_LINK_WIDTH);
        Link link = null;
        try {
          link = Link.newLink(fosBubble.get(fos1), fosBubble.get(fos2), width);
        } catch (IOException e) {
          throw new RuntimeException("Couldn't create link", e);
        }
        links.add(link);
      }
    }
  }

  private static void fillConnectionsMap(ResearchPaper paper, Map<String, Map<String, Integer>> connectedOccurenceMap) {
    for (String fos : paper.getFosWeights().keySet()) {
      for (String fos2 : paper.getFosWeights().keySet()) {
        if (fos.equals(fos2)) continue;
        String first = fos.compareTo(fos2) <= 0 ? fos : fos2; // order them in order to have all links for a couple of fos in one entry
        String second = first.equals(fos) ? fos2 : fos;
        Map<String, Integer> occMap = connectedOccurenceMap.computeIfAbsent(first, k -> new HashMap<>());
        int occ = occMap.getOrDefault(second, 0);
        occMap.put(second, occ + 1);
      }
    }
  }

  private static Bubble createBubble(Map<String, Bubble> fosBubbles, String fos, BubbleData data) {
    Bubble b = fosBubbles.get(fos);
    if (b != null) {
      ShaderUtils.setColor(b.getShader(), data.r, data.g, data.b);
      return b;
    }
    try {
      return Bubble.newBubble(fos, data.r, data.g, data.b, data.radius);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't create bubble", e);
    }
  }

  static public float toPercentage(float value, float min, float max) {
    return (value - min) / (max - min);
  }

  private static BubbleData toBubbleData(float radiusScore,float minRadiusScore, float maxRadiusScore, float citations, float minCitations, float maxCitations) {
    float radius = MIN_RADIUS + toPercentage(radiusScore, minRadiusScore, maxRadiusScore) * (MAX_RADIUS - MIN_RADIUS);
    float citationPercentage = toPercentage(citations, minCitations, maxCitations);
    float r = getColorField(Color::getR, START_COLOR, END_COLOR, citationPercentage);
    float g = getColorField(Color::getG, START_COLOR, END_COLOR, citationPercentage);
    float b = getColorField(Color::getB, START_COLOR, END_COLOR, citationPercentage);
    return new BubbleData(radius, r, g, b);
  }

  private static float getColorField(Function<Color, Float> fieldExtractor, Color start, Color end, float percentage) {
    return fieldExtractor.apply(start)
      + (fieldExtractor.apply(end) - fieldExtractor.apply(start))
      * percentage;
  }

  @AllArgsConstructor
  private static class BubbleData {
    private float radius;
    private float r;
    private float g;
    private float b;
  }

}
