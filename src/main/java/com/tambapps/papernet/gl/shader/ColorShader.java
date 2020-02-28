package com.tambapps.papernet.gl.shader;

import java.io.IOException;

public class ColorShader extends Shader {

  public static final String GREEN = "green";
  public static final String BLUE = "blue";
  public static final String ALPHA = "alpha";
  private static final String RED = "red";

  public ColorShader(String filename) throws IOException {
    super(filename);
    setUniformVariable(ALPHA, 1f);
  }

  public void setColor(float r, float g, float b) {
    setColor(r, g, b, getAlpha());
  }

  public void setColor(float r, float g, float b, float a) {
    setUniformVariable(RED, r);
    setUniformVariable(GREEN, g);
    setUniformVariable(BLUE, b);
    setUniformVariable(ALPHA, a);
  }

  public Color getColor() {
    float r = getUniformVariable(RED);
    float g = getUniformVariable(GREEN);
    float b = getUniformVariable(BLUE);
    float a = getUniformVariable(ALPHA);
    return new Color(r, g, b, a);
  }

  public void setColor(Color color) {
    setColor(color.getR(), color.getG(), color.getB(), color.getA());
  }

  public float getAlpha() {
    return getUniformVariable(ALPHA);
  }

  public void setAlpha(float alpha) {
    setUniformVariable(ALPHA, alpha);
  }
}
