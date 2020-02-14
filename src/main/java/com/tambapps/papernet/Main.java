package com.tambapps.papernet;

import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.gl.GlWindow;

import com.tambapps.papernet.gl.shader.Color;
import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shader.ShaderUtils;
import com.tambapps.papernet.gl.texture.Texture;
import com.tambapps.papernet.visualisation.drawable.Bubble;
import com.tambapps.papernet.visualisation.drawable.Bubbles;
import com.tambapps.papernet.visualisation.drawable.BubblesArranger;
import com.tambapps.papernet.visualisation.drawable.Link;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main extends GlWindow {

  private static final Color SELECTED_COLOR = new Color(0, 1, 0);

  private static final int NAVIGATION_OFFSET = 8;
  private static final float THRESHOLD_OFFSET = 0.1f;
  private static final float NAVIGATION_ZOOM_OFFSET = 0.0333333f;

  private static final Vector3f tempVec = new Vector3f();
  private Map<String, Bubble> fosBubbles = new HashMap<>();
  private List<Link> links;
  private Bubble selectedBubble = null;
  private Color selectedBubbleColor = null;

 // FontTT fontTT;
  private Texture texture;
  private float linkThreshold = Bubbles.MIN_LINK_WIDTH;
  private float bubbleThreshold = Bubbles.MIN_RADIUS;

  private final Collection<ResearchPaper> papers;

  public Main(Collection<ResearchPaper> papers) {
    this.papers = papers;
  }

  @Override
  public void onGlContextInitialized() throws IOException {
    System.out.println("Started initializing OpenGL...");
    long startTime = System.currentTimeMillis();
    links = new ArrayList<>();
    fosBubbles = Bubbles.toBubbles(fosBubbles, papers, links);
    shuffle(false);
    moveLinkThreshold(0); // to update links visibility
    texture = Texture.newTexture("background.jpg");
    try {
   //   fontTT = new FontTT(Font.createFont(Font.TRUETYPE_FONT, new File("/home/nelson/workspace/graphisme-visualisation-openGL/kenvector_future.ttf")), 16, 0);
    } catch (Exception e) {
      throw new IOException(e);
    }
    System.out.format("Finished intialization of OpenGL (in %ds)\n\n", (System.currentTimeMillis() - startTime) / 1000L);
    System.out.println("Press the arrow keys to move on the screen");
    System.out.println("pressed CTRL with up/down zoom/unzoom from the screen");
    System.out.println("Use E/D to modify the threshold of links that will be displayed");
    System.out.println("Use R/F to modify the threshold of bubbles that will be displayed");
    System.out.println("Use S to shuffle the bubbles");
    System.out.println("You can also touch and drag a bubble to move it");
    System.out.println("Use ESCAPE to exit");
  }
  // TODO implement bubble search through user input
  @Override
  public void onDraw(Matrix4f projection) {
    texture.bind();
    texture.draw();

    links.forEach(l -> l.draw(projection));
    fosBubbles.values()
      .stream()
    .sorted(Comparator.comparing(Bubble::getRadius).reversed()) // in decroissant order to draw big bubbles first
    .forEach(b -> b.draw(projection));
    //   fontTT.drawText("caca", 0.1f, 0, 0, 0, Color.white, 0, 0, 0, false);
  }

  @Override
  public void onLeftPressed(boolean ctrlPressed) {
    camera.addPosition(tempVec.set(NAVIGATION_OFFSET, 0, 0));
  }

  @Override
  public void onRightPressed(boolean ctrlPressed) {
    camera.addPosition(tempVec.set(- NAVIGATION_OFFSET, 0, 0));
  }

  @Override
  public void onUpPressed(boolean ctrlPressed) {
    if (ctrlPressed) {
      camera.zoomBy(- NAVIGATION_ZOOM_OFFSET);
    } else {
      camera.addPosition(tempVec.set(0, - NAVIGATION_OFFSET, 0));
    }
  }

  @Override
  public void onEscapePressed() {
    close();
  }

  @Override
  public void onDownPressed(boolean ctrlPressed) {
    if (ctrlPressed) {
      camera.zoomBy(NAVIGATION_ZOOM_OFFSET);
    } else {
      camera.addPosition(tempVec.set(0, NAVIGATION_OFFSET, 0));
    }
  }

  private void moveLinkThreshold(float offset) {
    if (linkThreshold <= Bubbles.MIN_LINK_WIDTH && offset < 0 ||
      linkThreshold >= Bubbles.MAX_LINK_WIDTH && offset > 0) {
      return;
    }
    linkThreshold += offset;
    links.forEach(l -> l.setVisible(l.getWidth() >= linkThreshold));

    System.out.format("updated link threshold: %f.1\n", (linkThreshold - Bubbles.MIN_LINK_WIDTH) / (Bubbles.MAX_LINK_WIDTH - Bubbles.MIN_LINK_WIDTH));
  }

  private void moveBubbleThreshold(float offset) {
    if (bubbleThreshold <= Bubbles.MIN_RADIUS && offset < 0 ||
      bubbleThreshold >= Bubbles.MAX_RADIUS && offset > 0) {
      return;
    }
    bubbleThreshold += offset;
    for (Bubble bubble : fosBubbles.values()) {
      bubble.setVisible(bubble.getRadius() >= bubbleThreshold);
    }
    links.forEach(Link::update);
    links.forEach(l -> l.setVisible(l.getWidth() >= linkThreshold)); // re update links visibility

    System.out.format("updated bubble threshold: %f.1\n", (linkThreshold - Bubbles.MIN_LINK_WIDTH) / (Bubbles.MAX_LINK_WIDTH - Bubbles.MIN_LINK_WIDTH));
  }

  @Override
  public void onKeyClicked(char c) {
    switch (c) {
      case 'e':
        moveLinkThreshold(THRESHOLD_OFFSET);
        break;
      case 'd':
        moveLinkThreshold(-THRESHOLD_OFFSET);
        break;
      case 'r':
        moveBubbleThreshold(THRESHOLD_OFFSET);
        break;
      case 'f':
        moveBubbleThreshold(-THRESHOLD_OFFSET);
        break;
      case 's': // shuffle
        shuffle(true);
        break;

    }
  }

  @Override
  public void update(float delta) {
    if (isOneAnimationRunning()) {
      links.forEach(Link::updatePos);
    }
  }

  private void shuffle(boolean withAnimation) {
    if (withAnimation) {
      clearAnimations();
      BubblesArranger.arrangeWithAnimation(fosBubbles.values(), this::addAnimation);
    } else {
      BubblesArranger.arrange(fosBubbles.values());
      links.forEach(Link::updatePos);
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Started loading...");
    long startTime = System.currentTimeMillis();
    ResearchPaperData data =  ResearchPaperDataParser.parseData();
    System.out.format("Loaded all data (in %ds)\n", (System.currentTimeMillis() - startTime) / 1000L);
    System.out.println("Here is the number of research paper by year:");
    data.getPapersByYear().forEach((key, value) ->
      System.out.format("%d -> %d papers", key, value.size()).println());
    System.out.println("Enter the year or 'all' for all papers");
    Collection<ResearchPaper> papers;
    try (Scanner scanner = new Scanner(System.in)) {
      String s =  args.length > 0 ? args[0] : scanner.nextLine();
      if (s.equals("all") || s.isEmpty()) {
        papers = data.getAllPapers();
      } else {
        papers = data.getPapersByYear().getOrDefault(Integer.parseInt(s), List.of());
      }
      new Main(papers).run();
    }
  }

  private boolean intersect(Bubble bubble, float x, float y) {
    return pow2(x - bubble.getX()) + pow2(y - bubble.getY() - 5) // - 10 is for the app bar TODO there is still an offset
      < pow2(bubble.getRadius());
  }

  private float pow2(float x) {
    return x * x;
  }

  @Override
  public void onTouchDown(float x, float y) {
    Vector3f projectPoint = projectPoint(x, y);
    selectedBubble = fosBubbles.values().stream()
      .filter(b -> intersect(b, projectPoint.x, projectPoint.y))
      .findFirst().orElse(null);
    if (selectedBubble != null) {
      Shader shader = selectedBubble.getShader();
      selectedBubbleColor = ShaderUtils.getColor(shader);
      ShaderUtils.setColor(shader, SELECTED_COLOR);
    }
  }

  private Vector3f projectPoint(float x, float y) {
    float zoom = camera.getZoom();
    Vector3f cameraPos = camera.getPosition();
    return tempVec.set((x - cameraPos.x) * zoom, (y - cameraPos.y) * zoom, 0);
  }

  @Override
  public void onTouchUp() {
    if (selectedBubble == null) {
      return;
    }
    ShaderUtils.setColor(selectedBubble.getShader(), selectedBubbleColor);
  }

  @Override
  public void onMouseDragged(float x, float y) {
    if (selectedBubble == null) {
      return;
    }
    Vector3f projectPoint = projectPoint(x, y);
    selectedBubble.setPosition(projectPoint.x, projectPoint.y);
    links.forEach(Link::updatePos);
  }
}