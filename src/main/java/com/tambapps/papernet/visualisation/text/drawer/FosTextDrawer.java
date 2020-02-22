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

  public static void drawFosTexts(Collection<Bubble> bubbles, Vector3f position, float zoom) {
    float zoomInverse = 1 / zoom;
    for (Bubble bubble : bubbles) {
      drawFos(bubble, position, zoomInverse);
    }
  }

  private static void drawFos(Bubble bubble, Vector3f position, float zoomInv) {
    Texture texture = FosTextTextureFactory.getTextureFor(bubble.getText());
    float percentage = MathUtils.toPercentage(bubble.getRadius(), BubblesNLink.MIN_RADIUS, BubblesNLink.MAX_RADIUS);

    texture.setHeightKeepRatio(zoomInv * ( MIN_TEXT_HEIGHT + percentage * percentage * 0.02f));
    texture.setPosition(zoomInv * (bubble.getX() + position.x) / HALF_WIDTH,
      zoomInv * (bubble.getY() + position.y) / HALF_HEIGHT);
    texture.bind();
    texture.draw();
  }
}
