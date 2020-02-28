package com.tambapps.papernet.visualisation.drawable;

import org.joml.Matrix4f;

abstract class AbstractDrawable implements Drawable {

  private boolean visible = true;

  @Override
  public final void draw(Matrix4f projection) {
    if (visible) {
      doDraw(projection);
    }
  }

  protected abstract void doDraw(Matrix4f projection);

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
}
