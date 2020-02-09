package com.tambapps.papernet;

import com.tambapps.papernet.gl.GlWindow;
import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shape.Circle;
import com.tambapps.papernet.gl.shape.Rectangle;
import com.tambapps.papernet.gl.text.Text;

import java.io.IOException;

public class Main extends GlWindow {

  Rectangle rectangle;
  Shader shader;

  Main() throws IOException {
    rectangle = new Rectangle();
  }

  @Override
  public void onGlContextInitialized() throws IOException {
    shader = new Shader("shader");

  }

  @Override
  public void onDraw() {
    shader.bind();
    shader.setUniform("green", 1);
    //rectangle.draw();
    Text.drawString("test", 0, 0, 0.3f, 1000);
  }

  public static void main(String[] args) throws IOException {
    new Main().run();
  }

}