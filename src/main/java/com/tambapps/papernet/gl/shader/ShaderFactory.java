package com.tambapps.papernet.gl.shader;

import java.io.IOException;

public class ShaderFactory {


  public static Shader rgbShader(float r, float g, float b) throws IOException {
    Shader shader = new Shader("shader");
    ShaderUtils.setColor(shader, r, g, b);
    return shader;
  }
}
