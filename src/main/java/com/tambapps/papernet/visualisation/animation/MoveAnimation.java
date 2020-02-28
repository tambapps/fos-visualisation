package com.tambapps.papernet.visualisation.animation;

import com.tambapps.papernet.visualisation.animation.interpolation.Interpolation;
import com.tambapps.papernet.visualisation.drawable.Movable;
import lombok.Data;

@Data
public class MoveAnimation extends Animation {

  private Movable movable;
  private float startX, startY;
  private float endX, endY;

  public MoveAnimation(Movable movable, float endX, float endY, float duration,
      Interpolation interpolation) {
    this(movable, endX, endY, duration);
    setInterpolation(interpolation);
  }

  public MoveAnimation(Movable movable, float endX, float endY, float duration) {
    super(duration);
    this.movable = movable;
    this.endX = endX;
    this.endY = endY;
  }

  protected void begin() {
    startX = movable.getX();
    startY = movable.getY();
  }

  protected void update(float percent) {
    float x, y;
    if (percent == 0) {
      x = startX;
      y = startY;
    } else if (percent == 1) {
      x = endX;
      y = endY;
    } else {
      x = startX + (endX - startX) * percent;
      y = startY + (endY - startY) * percent;
    }
    movable.setPosition(x, y);
  }

}
