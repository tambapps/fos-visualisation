package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.gl.shader.ColorShader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Circle;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.Objects;

public class Bubble extends AbstractDrawable implements Movable {

  private Circle circle;
  private ColorShader shader;
  private String fos;

  private Bubble(float radius, ColorShader shader, String fos) {
    this.circle = new Circle(radius);
    this.shader = shader;
    this.fos = fos;
  }

  public static Bubble newBubble(String text, float r, float g, float b, float radius) throws IOException {
    return new Bubble(radius, ShaderFactory.rgbShader(r, g, b), text);
  }

  @Override
  protected void doDraw(Matrix4f projection) {
    shader.bind(projection);
    circle.draw();
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

  public String getFos() {
    return fos;
  }

  @Override
  public void setPosition(float x, float y) {
    setX(x);
    setY(y);
  }

  @Override
  public void setAlpha(float alpha) {
    shader.setAlpha(alpha);
  }

  @Override
  public float getAlpha() {
    return shader.getAlpha();
  }

  public void setRadius(float radius) {
    circle.setRadius(radius);
  }

  public ColorShader getShader() {
    return shader;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bubble bubble = (Bubble) o;
    return Objects.equals(fos, bubble.fos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fos);
  }
}
