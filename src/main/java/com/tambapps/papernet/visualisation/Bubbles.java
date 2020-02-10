package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.FosData;
import com.tambapps.papernet.data.WeightedCitation;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Bubbles {

  private static final float MAX_RADIUS = 0.5f;
  private static final float MIN_RADIUS = 0.1f;

  private static float radiusScore(List<WeightedCitation> radiusScore) {
    return radiusScore.stream()
      .map(s -> s.getWeight() * 100f)
      .reduce(0f, Float::sum);
  }

  public static List<Bubble> toBubbles(Collection<ResearchPaper> researchPapers) {
    Map<String, List<WeightedCitation>> fosWeightedCitations = FosData.getFosWeightedCitations(researchPapers);
    Map<String, Float> radiusScore = fosWeightedCitations.entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> radiusScore(e.getValue())));
    float maxScore = Float.MIN_VALUE;
    float minScore = Float.MAX_VALUE;
    for (Float score : radiusScore.values()) {
      if (maxScore < score) {
        maxScore = score;
      }
      if (minScore > score) {
        minScore = score;
      }
    }

    float finalMinScore = minScore;
    float finalMaxScore = maxScore;
    Map<String, BubbleData> fosBubbleData = fosWeightedCitations.entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> Bubbles.toBubbleData(radiusScore.get(e.getKey()), finalMinScore, finalMaxScore)));

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

  private static BubbleData toBubbleData(float radiusScore,float minRadiusScore, float maxRadiusScore) {
    float radius = MIN_RADIUS + toPercentage(radiusScore, minRadiusScore, maxRadiusScore) * (MAX_RADIUS - MIN_RADIUS);
    float r= 1, g= 1, b = 1;
    return new BubbleData(radius, r, g, b);
  }


  @AllArgsConstructor
  private static class BubbleData {
    private float radius;
    private float r;
    private float g;
    private float b;
  }
}
