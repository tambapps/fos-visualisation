package com.tambapps.papernet.visualisation.text.drawer;

import com.tambapps.papernet.gl.GlWindow;
import com.tambapps.papernet.gl.text.FosTextTextureFactory;
import com.tambapps.papernet.gl.texture.Texture;
import com.tambapps.papernet.util.MathUtils;
import com.tambapps.papernet.visualisation.drawable.Bubble;
import com.tambapps.papernet.visualisation.drawable.BubblesNLink;
import org.joml.Vector3f;

import java.util.Collection;

public class FosTextDrawer {

  private static final float MIN_TEXT_HEIGHT = 0.01f;
  private static final float HALF_WIDTH = GlWindow.WINDOW_WIDTH >> 1;
  private static final float HALF_HEIGHT = GlWindow.WINDOW_HEIGHT >> 1;

  public static void drawFosTexts(Collection<Bubble> bubbles, Vector3f cameraPosition, float zoom) {
    float zoomInverse = 1 / zoom;
    bubbles.stream()
      .filter(Bubble::isVisible)
      .forEach(bubble -> drawFos(bubble, cameraPosition, zoomInverse));
  }

  private static void drawFos(Bubble bubble, Vector3f cameraPosition, float zoomInv) {
    String fos = bubble.getFos();
    Texture texture = FosTextTextureFactory.getTextureFor(fos);
    float percentage = MathUtils.toPercentage(bubble.getRadius(), BubblesNLink.MIN_RADIUS, BubblesNLink.MAX_RADIUS);
    float height = nbLines(fos) * zoomInv * (MIN_TEXT_HEIGHT + percentage * 0.02f);
    texture.setHeightKeepRatio(height);
    texture.setPosition(zoomInv * (bubble.getX() + cameraPosition.x) / HALF_WIDTH,
      zoomInv * (bubble.getY() + cameraPosition.y) / HALF_HEIGHT - height / 2f);
    texture.bind();
    texture.draw();
  }

  private static int nbLines(String fos) {
    return 1 + (int) fos.chars().filter(c -> c == ' ').count() / 2;
  }
}
