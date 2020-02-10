package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Circle;
import com.tambapps.papernet.gl.text.Text;

import java.io.IOException;

public class Bubble {

  private Shader shader;
  private Circle circle;
  private String text;

  private Bubble(Shader shader, Circle circle, String text) {
    this.shader = shader;
    this.circle = circle;
    this.text = text;
  }

  public static Bubble newBubble(String text, float r, float g, float b, float radius) throws IOException {
    return new Bubble(ShaderFactory.rgbShader(r, g, b), new Circle(radius), text);
  }

  public void draw() {
    circle.draw();
    shader.bind();
    Text.drawString(text, circle.getX() - circle.getRadius() / 2, circle.getY() - circle.getRadius() / 2,
      0.2f, circle.getRadius() * 40);
  }

}
