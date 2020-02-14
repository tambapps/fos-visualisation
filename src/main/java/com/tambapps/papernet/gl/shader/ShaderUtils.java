package com.tambapps.papernet.gl.shader;

public class ShaderUtils {

  public static void setColor(Shader shader, Color color) {
    setColor(shader, color.getR(), color.getG(), color.getB());
  }

  public static void setColor(Shader shader, float r, float g, float b) {
    shader.setUniformVariable("red", r);
    shader.setUniformVariable("green", g);
    shader.setUniformVariable("blue", b);
  }

  public static Color getColor(Shader shader) {
    float r = shader.getUniformVariable("red");
    float g = shader.getUniformVariable("green");
    float b = shader.getUniformVariable("blue");
    return new Color(r, g, b);
  }

}
