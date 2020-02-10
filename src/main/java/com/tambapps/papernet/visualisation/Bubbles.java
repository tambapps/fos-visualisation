package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.WeightedCitation;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Bubbles {

  private static Color START_COLOR = new Color(0, 0, 1);
  private static Color END_COLOR = new Color(1, 0, 0);

  private static final float MAX_RADIUS = 0.5f;
  private static final float MIN_RADIUS = 0.05f;

  private static float radiusScore(List<WeightedCitation> radiusScore) {
    return radiusScore.stream()
      .map(s -> 100 + s.getWeight() * 100f)
      .reduce(0f, Float::sum);
  }

  public static List<Bubble> toBubbles(Collection<ResearchPaper> researchPapers) {
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

    // TODO order by radius decroissant
    return fosBubbleData.entrySet()
      .stream()
      .map(e -> createBubble(e.getKey(), e.getValue()))
      .collect(Collectors.toList());
  }

  private static Bubble createBubble(String fos, BubbleData data) {
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

  @Value
  private static class Color {
    private float r;
    private float g;
    private float b;
  }
}