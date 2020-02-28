package com.tambapps.papernet.visualisation.drawable;

import org.joml.Matrix4f;

public interface Drawable {

  void draw(Matrix4f projection);

  boolean isVisible();

  void setVisible(boolean visible);

  float getAlpha();

  void setAlpha(float alpha);
}
