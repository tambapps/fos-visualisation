package com.tambapps.papernet.visualisation.animation.interpolation;

public interface Interpolation {
  // fast then slow
  Interpolation POW2_FAST_THEN_SLOW = f -> (f - 1) * (f - 1) * -1 + 1;

  float apply(float percent);

}
