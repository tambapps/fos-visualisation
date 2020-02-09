package com.tambapps.papernet.gl.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class Shader {

  private int programm;
  private int vs; // vertex shader id
  private int fs; // fragment shader id

  public Shader(String filename) throws IOException {
    programm = glCreateProgram();
    vs = createShader(GL_VERTEX_SHADER, filename + ".vs");
    fs = createShader(GL_FRAGMENT_SHADER, filename + ".fs");

    glAttachShader(programm, vs);
    glAttachShader(programm, fs);

    glBindAttribLocation(programm, 0, "vertices");
    glLinkProgram(programm);
    if (glGetProgrami(programm, GL_LINK_STATUS) != 1) {
      throw new IOException("Couldn't link program: " + glGetProgramInfoLog(programm));
    }
    glValidateProgram(programm);
    if (glGetProgrami(programm, GL_VALIDATE_STATUS) != 1) {
      throw new IOException("Couldn't link program: " + glGetProgramInfoLog(programm));
    }
  }

  public void setUniform(String name, int value) {
    int location = glGetUniformLocation(programm, name);
    if (location == -1) {
      return; // location doesn't exists
    }
    glUniform1i(location, value);
  }

  public void bind() {
    glUseProgram(programm);
  }

  private int createShader(int glShaderType, String filename) throws IOException {
    int shader = glCreateShader(glShaderType);
    glShaderSource(shader, readFile(filename));
    glCompileShader(shader);
    // check if error on shader
    if (glGetShaderi(shader, GL_COMPILE_STATUS) != 1) {
      throw new IOException("Couldn't compile shader: " + glGetShaderInfoLog(shader));
    }
    return shader;
  }
  private String readFile(String filename) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
      Shader.class.getResourceAsStream("/shaders/" + filename)
    ))) {
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }
}