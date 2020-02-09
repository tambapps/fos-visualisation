package com.tambapps.papernet.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FosData {

  private final Map<Integer, Fos> fosById;
  private final Map<Integer, Fos> fosByYear;
  private final Map<String, Integer> fosOccurenceMap;

  public Map<String, List<Float>> getAllFosWeights(Collection<Fos> foses) {
    Map<String, List<Float>> fosWeights = new HashMap<>();
    foses.stream()
      .map(Fos::getFosWeights)
      .flatMap(map -> map.entrySet().stream())
      .forEach(entry -> {
        List<Float> weights = fosWeights.computeIfAbsent(entry.getKey(),
          k -> new ArrayList<>());
        weights.add(entry.getValue());
      });
    return fosWeights;
  }
}
