package com.tambapps.papernet.visualisation.drawable.arranger;

import com.tambapps.papernet.visualisation.animation.Animation;
import com.tambapps.papernet.visualisation.animation.MoveAnimation;
import com.tambapps.papernet.visualisation.animation.interpolation.Interpolation;
import com.tambapps.papernet.visualisation.drawable.Bubble;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class BubblesArranger {

  private static final Vector3f tempVec = new Vector3f();

  private static boolean overlaps(float x1, float y1, float radius1, float x2, float y2, float radius2) {
    float xDif = x1 - x2;
    float yDif = y1 - y2;
    float distanceSquared = xDif * xDif + yDif * yDif;
    radius1 = radius1 * 2f; // in order to have some space between each bubble
    return distanceSquared < (radius1 + radius2) * (radius1 + radius2);
  }

  private static boolean overlaps(Bubble b1, Bubble b2) {
    return overlaps(b1.getX(), b1.getY(), b1.getRadius(),
      b2.getX(), b2.getY(), b2.getRadius());
  }

  private static boolean overlaps(Bubble bubble, Collection<Bubble> bubbles) {
    return bubbles.stream()
      .anyMatch(b -> overlaps(bubble, b));
  }

  public static void arrange(Collection<Bubble> bubbles) {
    arrange(bubbles, new ArrayList<>());
  }

  public static void arrange(Collection<Bubble> bubbles, List<Bubble> arrangedBubbles) {
    PositionArranger positionArranger = new PositionArranger();
    for (Bubble bubble : bubbles) {
      positionArranger.init();
      while (true) {
        Vector3f position = positionArranger.arrange(bubble, tempVec);
        bubble.setX(position.x);
        bubble.setY(position.y);
        if (!overlaps(bubble, arrangedBubbles)) {
          arrangedBubbles.add(bubble);
          break;
        }
      }
    }
  }
  private static boolean overlaps(Vector3f endPosition, Collection<Vector3f> bubblePositions) {
    return bubblePositions.stream()
      .anyMatch(position -> overlaps(endPosition.x, endPosition.y, endPosition.z,
        position.x, position.y, position.z));
  }

  public static void arrangeWithAnimation(Collection<Bubble> bubbles, Consumer<Animation> animationConsumer) {
    List<Vector3f> arrangedBubblePositions = new ArrayList<>(); // x, y, radius
    PositionArranger positionArranger = new PositionArranger();
    for (Bubble bubble : bubbles) {
      positionArranger.init();
      while (true) {
        Vector3f endPosition = positionArranger.arrange(bubble, tempVec);
        if (!overlaps(endPosition, arrangedBubblePositions)) {
          animationConsumer.accept(new MoveAnimation(bubble, endPosition.x, endPosition.y, 1f, Interpolation.POW2_FAST_THEN_SLOW));
          arrangedBubblePositions.add(new Vector3f(endPosition));
          break;
        }
      }
    }
  }
}