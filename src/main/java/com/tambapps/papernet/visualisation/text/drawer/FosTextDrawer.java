package com.tambapps.papernet.visualisation.text.drawer;

import com.tambapps.papernet.gl.GlWindow;
import com.tambapps.papernet.gl.text.FosTextTextureFactory;
import com.tambapps.papernet.gl.texture.Texture;
import com.tambapps.papernet.util.MathUtils;
import com.tambapps.papernet.visualisation.drawable.Node;
import com.tambapps.papernet.visualisation.drawable.FosNetUtils;
import org.joml.Vector3f;

import java.util.Collection;

public class FosTextDrawer {

  private static final float MIN_TEXT_HEIGHT = 0.02f;
  private static final float MAX_TEXT_HEIGHT = 0.055f;
  private static final float HALF_WIDTH = GlWindow.WINDOW_WIDTH >> 1;
  private static final float HALF_HEIGHT = GlWindow.WINDOW_HEIGHT >> 1;

  public static void drawFosTexts(Collection<Node> nodes, Vector3f cameraPosition, float zoom) {
    float zoomInverse = 1 / zoom;
    nodes.stream()
        .filter(Node::isVisible)
        .forEach(node -> drawFos(node, cameraPosition, zoomInverse));
  }

  private static void drawFos(Node node, Vector3f cameraPosition, float zoomInv) {
    String fos = node.getFos();
    Texture texture = FosTextTextureFactory.getTextureFor(fos);
    float percentage = MathUtils
        .toPercentage(node.getRadius(), FosNetUtils.MIN_RADIUS, FosNetUtils.MAX_RADIUS);
    float height = nbLines(fos) * zoomInv * (MIN_TEXT_HEIGHT + percentage * (MAX_TEXT_HEIGHT
        - MIN_TEXT_HEIGHT));
    texture.setHeightKeepRatio(height);
    texture.setPosition(zoomInv * (node.getX() + cameraPosition.x) / HALF_WIDTH,
        zoomInv * (node.getY() + cameraPosition.y) / HALF_HEIGHT - height / 2f);
    texture.bind();
    texture.draw();
  }

  private static int nbLines(String fos) {
    return 1 + (int) fos.chars().filter(c -> c == ' ').count() / 2;
  }
}
