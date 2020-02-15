package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.gl.shader.ColorShader;
import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Circle;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.Objects;

public class Bubble extends AbstractDrawable implements Movable {

  private Circle circle;
  private ColorShader shader;
  private Shader textShader;
  private String text;

  private Bubble(float radius, ColorShader shader, String text, Shader textShader) {
    this.circle = new Circle(radius);
    this.shader = shader;
    this.text = text;
    this.textShader = textShader;
  }

  public static Bubble newBubble(String text, float r, float g, float b, float radius) throws IOException {
    return new Bubble(radius, ShaderFactory.rgbShader(r, g, b), text, ShaderFactory.rgbShader(1, 0, 0));
  }

  @Override
  protected void doDraw(Matrix4f projection) {
    shader.bind(projection);
    circle.draw();
    textShader.bind(projection);
  //  glUseProgram(0);
    // TODO draw text
   // Text.drawString(text, circle.getX() - circle.getRadius() / 2, circle.getY() - circle.getRadius() / 2,
     // 0.2f, 40);
  }

  public float getRadius() {
    return circle.getRadius();
  }

  public float getX() {
    return circle.getX();
  }

  public float getY() {
    return circle.getY();
  }

  public void setX(float x) {
    circle.setX(x);
  }

  public void setY(float y) {
    circle.setY(y);
  }

  public String getText() {
    return text;
  }

  @Override
  public void setPosition(float x, float y) {
    setX(x);
    setY(y);
  }

  public ColorShader getShader() {
    return shader;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bubble bubble = (Bubble) o;
    return Objects.equals(text, bubble.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }
}
