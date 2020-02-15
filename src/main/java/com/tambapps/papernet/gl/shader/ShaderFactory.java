package com.tambapps.papernet.gl.shader;

import com.tambapps.papernet.visualisation.drawable.Link;

import java.io.IOException;

public class ShaderFactory {

  private static ColorShader linkShader;

  public static ColorShader linksShader() {
    return linkShader;
  }

  public static void setLinkShader(ColorShader linkShader) {
    ShaderFactory.linkShader = linkShader;
  }

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
