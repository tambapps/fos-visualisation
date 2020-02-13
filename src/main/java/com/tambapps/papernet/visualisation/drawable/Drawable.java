package com.tambapps.papernet.visualisation.drawable;

import org.joml.Matrix4f;

public interface Drawable {

  void draw(Matrix4f projection);

  void setVisible(boolean visible);

  boolean isVisible();
}
