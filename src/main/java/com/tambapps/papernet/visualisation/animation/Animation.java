package com.tambapps.papernet.visualisation.animation;

import com.tambapps.papernet.visualisation.animation.interpolation.Interpolation;
import lombok.Data;

@Data
abstract class Animation {
  private float duration, time;
  private Interpolation interpolation;
  private boolean reverse, began, complete;

  public Animation () {
  }

  public Animation (float duration) {
    this.duration = duration;
  }

  public Animation (float duration, Interpolation interpolation) {
    this.duration = duration;
    this.interpolation = interpolation;
  }

  public boolean act (float delta) {
    if (complete) return true;
    if (!began) {
      begin();
      began = true;
    }
    time += delta;
    complete = time >= duration;
    float percent = complete ? 1 : time / duration;
    if (interpolation != null) percent = interpolation.apply(percent);
    update(reverse ? 1 - percent : percent);
    if (complete) end();
    return complete;
  }

  protected void begin () {
  }

  protected void end () {
  }

  abstract protected void update (float percent);


  public void finish () {
    time = duration;
  }

  public void restart () {
    time = 0;
    began = false;
    complete = false;
  }

  public void reset () {
    reverse = false;
    interpolation = null;
  }

}
