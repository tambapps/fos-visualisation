package com.tambapps.papernet.visualisation;

import org.joml.Matrix4f;

public interface Drawable {

  void draw(Matrix4f projection);

  void setVisible(boolean visible);

}
