package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Circle;
import com.tambapps.papernet.gl.text.Text;
import org.joml.Matrix4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class Bubble extends Circle {

  private Shader shader;
  private Shader textShader;
  private String text;

  private Bubble(float radius, Shader shader, String text, Shader textShader) {
    super(radius);
    this.shader = shader;
    this.text = text;
    this.textShader = textShader;
  }

  public static Bubble newBubble(String text, float r, float g, float b, float radius) throws IOException {
    return new Bubble(radius, ShaderFactory.rgbShader(r, g, b), text, ShaderFactory.rgbShader(1, 0, 0));
  }

  public void draw() {
    throw new UnsupportedOperationException("You should used draw(Matrix4f)");
  }

  public void draw(Matrix4f projection) {
    shader.bind(projection);
    super.draw();
    textShader.bind(projection);
  //  glUseProgram(0);
    // TODO draw text
   // Text.drawString(text, circle.getX() - circle.getRadius() / 2, circle.getY() - circle.getRadius() / 2,
     // 0.2f, 40);
  }

  public String getText() {
    return text;
  }
}
