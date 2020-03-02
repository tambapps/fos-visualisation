package com.tambapps.papernet.visualisation.drawable.arranger;

import com.tambapps.papernet.visualisation.animation.Animation;
import com.tambapps.papernet.visualisation.animation.MoveAnimation;
import com.tambapps.papernet.visualisation.animation.interpolation.Interpolation;
import com.tambapps.papernet.visualisation.drawable.Node;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class NodesArranger {

  private static final Vector3f tempVec = new Vector3f();

  private static boolean overlaps(float x1, float y1, float radius1, float x2, float y2,
      float radius2) {
    float xDif = x1 - x2;
    float yDif = y1 - y2;
    float distanceSquared = xDif * xDif + yDif * yDif;
    radius1 = radius1 * 2f; // in order to have some space between each node
    return distanceSquared < (radius1 + radius2) * (radius1 + radius2);
  }

  private static boolean overlaps(Node b1, Node b2) {
    return overlaps(b1.getX(), b1.getY(), b1.getRadius(),
        b2.getX(), b2.getY(), b2.getRadius());
  }

  private static boolean overlaps(Node node, Collection<Node> nodes) {
    return nodes.stream()
        .anyMatch(b -> overlaps(node, b));
  }

  public static void arrange(Collection<Node> nodes) {
    arrange(nodes, new ArrayList<>());
  }

  public static void arrange(Collection<Node> nodes, List<Node> arrangedNodes) {
    PositionArranger positionArranger = new PositionArranger();
    for (Node node : nodes) {
      positionArranger.init();
      while (true) {
        Vector3f position = positionArranger.arrange(node, tempVec);
        node.setX(position.x);
        node.setY(position.y);
        if (!overlaps(node, arrangedNodes)) {
          arrangedNodes.add(node);
          break;
        }
      }
    }
  }

  private static boolean overlaps(Vector3f endPosition, Collection<Vector3f> nodePositions) {
    return nodePositions.stream()
        .anyMatch(position -> overlaps(endPosition.x, endPosition.y, endPosition.z,
            position.x, position.y, position.z));
  }

  public static void arrangeWithAnimation(Collection<Node> nodes,
      Consumer<Animation> animationConsumer) {
    List<Vector3f> arrangedNodePositions = new ArrayList<>(); // x, y, radius
    PositionArranger positionArranger = new PositionArranger();
    for (Node node : nodes) {
      positionArranger.init();
      while (true) {
        Vector3f endPosition = positionArranger.arrange(node, tempVec);
        if (!overlaps(endPosition, arrangedNodePositions)) {
          animationConsumer.accept(new MoveAnimation(node, endPosition.x, endPosition.y, 1f,
              Interpolation.POW2_FAST_THEN_SLOW));
          arrangedNodePositions.add(new Vector3f(endPosition));
          break;
        }
      }
    }
  }
}
