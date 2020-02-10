package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Circle;
import com.tambapps.papernet.gl.text.Text;

import java.io.IOException;

public class Bubble {

  private Shader shader;
  private Shader textShader;
  private Circle circle;
  private String text;

  private Bubble(Shader shader, Circle circle, String text, Shader textShader) {
    this.shader = shader;
    this.circle = circle;
    this.text = text;
    this.textShader = textShader;
  }

  public static Bubble newBubble(String text, float r, float g, float b, float radius) throws IOException {
    return new Bubble(ShaderFactory.rgbShader(r, g, b), new Circle(radius), text, ShaderFactory.rgbShader(0, 0, 0));
  }

  public void draw() {
    shader.bind();
    circle.draw();
    textShader.bind();
    Text.drawString(text, circle.getX() - circle.getRadius() / 2, circle.getY() - circle.getRadius() / 2,
      0.2f, circle.getRadius() * 40);
  }

  public void setX(float x) {
    circle.setX(x);
  }

  public void setY(float y) {
    circle.setY(y);
  }

}
