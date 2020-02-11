package com.tambapps.papernet.visualisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tambapps.papernet.gl.GlWindow.WINDOW_HEIGHT;
import static com.tambapps.papernet.gl.GlWindow.WINDOW_WIDTH;

public class BubblesArranger {

  private static boolean overlaps(Bubble b1, Bubble b2) {
    float xDif = b1.getX() - b2.getX();
    float yDif = b1.getY() - b2.getY();
    float distanceSquared = xDif * xDif + yDif * yDif;
    float radius1 = b1.getRadius() * 2f; // in order to have some space between each bubble
    float radius2 = b2.getRadius();
    return distanceSquared < (radius1 + radius2) * (radius1 + radius2);
  }

  private static boolean overlaps(Bubble bubble, Collection<Bubble> bubbles) {
    return bubbles.stream()
      .filter(b -> b != bubble)
      .anyMatch(b -> overlaps(bubble, b));
  }

  public static void arrange(Collection<Bubble> bubbles) {
    List<Bubble> arrangedBubbles = new ArrayList<>();
    for (Bubble bubble : bubbles) {
      boolean placed = false;
      while (!placed) {
        bubble.setX((float)Math.random() * WINDOW_WIDTH - (WINDOW_WIDTH >> 1));
        bubble.setY((float)Math.random() * WINDOW_HEIGHT - (WINDOW_HEIGHT >> 1));
        if (!overlaps(bubble, arrangedBubbles)) {
          placed = true;
        }
        arrangedBubbles.add(bubble);
      }
    }
  }

}
