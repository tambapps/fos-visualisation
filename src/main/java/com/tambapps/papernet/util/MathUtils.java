package com.tambapps.papernet.util;

public class MathUtils {

  public static float toPercentage(float value, float min, float max) {
    return (value - min) / (max - min);
  }

  public static float percentageMapping(float value, float min, float max, float mapMin, float mapMax) {
    return mapMin + toPercentage(value, min, max) * (mapMax - mapMin);
  }
}
