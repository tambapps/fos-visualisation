package com.tambapps.papernet.visualisation;

import java.util.Collection;

import static com.tambapps.papernet.gl.GlWindow.WINDOW_HEIGHT;
import static com.tambapps.papernet.gl.GlWindow.WINDOW_WIDTH;

public class BubblesArranger {

  public static void arrange(Collection<Bubble> bubbles) {
    for (Bubble bubble : bubbles) {
      bubble.setX((float)Math.random() * WINDOW_WIDTH - (WINDOW_WIDTH >> 1));
      bubble.setY((float)Math.random() * WINDOW_HEIGHT - (WINDOW_HEIGHT >> 1));
    }
  }

}
