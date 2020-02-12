package com.tambapps.papernet.visualisation.animation.interpolation;

public interface Interpolation {

  float apply(float percent);

  static Interpolation linear() {
    return f -> f;
  }

  // fast then slow
  static Interpolation pow2Out() {
    return f ->  (f -1) * (f - 1) * -1  + 1;
  }

}
