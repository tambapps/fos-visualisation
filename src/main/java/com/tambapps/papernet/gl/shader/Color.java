package com.tambapps.papernet.gl.shader;

import lombok.Value;

@Value
public class Color {
  private float r;
  private float g;
  private float b;
  private float a;

  public Color(float r, float g, float b) {
    this(r, g, b ,1f);
  }

}