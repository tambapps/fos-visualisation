package com.tambapps.papernet.visualisation.text.drawer;

import com.tambapps.papernet.gl.GlWindow;
import com.tambapps.papernet.gl.text.FosTextTextureFactory;
import com.tambapps.papernet.gl.texture.Texture;
import com.tambapps.papernet.visualisation.drawable.Bubble;
import org.joml.Vector3f;

import java.util.Collection;

public class FosTextDrawer {

  private static final float HALF_WIDTH = GlWindow.WINDOW_WIDTH >> 1;
  private static final float HALF_HEIGHT = GlWindow.WINDOW_HEIGHT >> 1;

  public static void drawFosTexts(Collection<Bubble> bubbles, Vector3f position, float zoom) {
    float zoomInverse = 1 / zoom;
    for (Bubble bubble : bubbles) {
      drawFos(bubble, position, zoomInverse);
    }
  }

  private static void drawFos(Bubble bubble, Vector3f position, float zoomInv) {
    Texture texture = FosTextTextureFactory.getTextureFor(bubble.getText());
    texture.setHeightKeepRatio(zoomInv * 0.03f);
    texture.setPosition(zoomInv * (bubble.getX() + position.x) / HALF_WIDTH,
      zoomInv * (bubble.getY() + position.y) / HALF_HEIGHT);
    texture.bind();
    texture.draw();
  }
}
