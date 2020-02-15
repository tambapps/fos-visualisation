package com.tambapps.papernet.visualisation.animation;

import com.tambapps.papernet.visualisation.drawable.Drawable;

public class AlphaAnimation extends Animation {

  private final Drawable drawable;

  public AlphaAnimation(Drawable drawable, float duration) {
    this.drawable = drawable;
    setDuration(duration);
  }

  public AlphaAnimation(Drawable drawable, float duration, boolean reversed, Runnable onEnd) {
    this(drawable, duration);
    setReverse(reversed);
    setOnEnd(onEnd);
  }

  @Override
  protected void update(float percent) {
    drawable.setAlpha(percent);
  }
}
