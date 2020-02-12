package com.tambapps.papernet.visualisation.animation.interpolation;

public interface Interpolation {

  float apply(float percent);

  static Interpolation linear() {
    return f -> f;
  }

  static Interpolation pow2() {
    return f -> f * f;
  }

}
