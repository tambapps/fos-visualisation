package com.tambapps.papernet;

import com.tambapps.papernet.gl.GlWindow;
import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shader.ShaderFactory;

import com.tambapps.papernet.gl.shape.Rectangle;
import com.tambapps.papernet.gl.text.Text;
import com.tambapps.papernet.visualisation.Bubble;

import java.io.IOException;

// TODO UTILISER TEXTURE EN BACKGROUND
public class Main extends GlWindow {

  Bubble bubble;
  Rectangle rectangle;
  Shader shader;

  Main() throws IOException {
    rectangle = new Rectangle();
  }

  @Override
  public void onGlContextInitialized() throws IOException {
    shader = ShaderFactory.rgbShader(1, 1f, 0.5f);
    shader.setUniformVariable("red", 1f);
    shader.setUniformVariable("green", 1f);
    shader.setUniformVariable("blue", 0.5f);

    rectangle.setX(-1 + rectangle.getWidth() / 2 + 0.1f);
bubble = Bubble.newBubble("caca", 0, 1, 0, 0.2f);
  }

  @Override
  public void onDraw() {
    shader.bind();
    rectangle.draw();
    Text.drawString("test", 0, 0, 0.3f, 1000);
    bubble.draw();
  }

  public static void main(String[] args) throws IOException {
    new Main().run();
  }

}