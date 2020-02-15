package com.tambapps.papernet.gl.shader;

import java.io.IOException;

public class ShaderFactory {

  public static ColorShader rgbaShader(float r, float g, float b, float a) throws IOException {
    ColorShader shader = new ColorShader("shader");
    shader.setColor(r, g, b, a);
    return shader;
  }

    public static ColorShader rgbShader(float r, float g, float b) throws IOException {
    ColorShader shader = new ColorShader("shader");
    shader.setColor(r, g, b);
    return shader;
  }
}
