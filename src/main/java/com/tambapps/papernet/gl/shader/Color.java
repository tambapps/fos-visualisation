package com.tambapps.papernet.gl.shader;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Color {
  private float r;
  private float g;
  private float b;
  private float a;

  public Color(float r, float g, float b) {
    this(r, g, b, 1f);
  }

}