package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.visualisation.animation.Animation;
import com.tambapps.papernet.visualisation.animation.MoveAnimation;
import com.tambapps.papernet.visualisation.animation.interpolation.Interpolation;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.tambapps.papernet.gl.GlWindow.WINDOW_HEIGHT;
import static com.tambapps.papernet.gl.GlWindow.WINDOW_WIDTH;

public class BubblesArranger {

  private static final Vector2f tempVec = new Vector2f();

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
  private static boolean overlaps(Bubble bubble, Vector2f endPosition, Collection<Bubble> bubbles) {
    float originalX = bubble.getX();
    float originalY = bubble.getY();
    try {
      bubble.setPosition(endPosition.x, endPosition.y);
      return overlaps(bubble, bubbles);
    } finally {
      bubble.setPosition(originalX, originalY);
    }
  }

  // TODO BUG, SOME BUBLES OVERLAP EACH OTHERS
  public static void arrangeWithAnimation(Collection<Bubble> bubbles, Consumer<Animation> animationConsumer) {
    Map<Bubble, Vector2f> bubbleEndPositionMap = new HashMap<>();
    for (Bubble bubble : bubbles) {
      boolean placed = false;
      while (!placed) {
        Vector2f endPosition = tempVec.set(
          (float)Math.random() * WINDOW_WIDTH - (WINDOW_WIDTH >> 1),
          (float)Math.random() * WINDOW_HEIGHT - (WINDOW_HEIGHT >> 1)
        );
        if (!overlaps(bubble, endPosition, bubbleEndPositionMap.keySet())) {
          placed = true;
          animationConsumer.accept(new MoveAnimation(bubble, endPosition.x, endPosition.y, 1f, Interpolation.linear()));
          bubbleEndPositionMap.put(bubble, endPosition);
        }
      }
    }
  }
}
