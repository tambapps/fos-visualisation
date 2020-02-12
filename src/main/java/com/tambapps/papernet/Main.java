package com.tambapps.papernet;

import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.gl.GlWindow;

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
import java.util.List;
import java.util.Scanner;

public class Main extends GlWindow {

  private static final int NAVIGATION_OFFSET = 8;
  private static final float LINK_THRESHOLD_OFFSET = 0.1f;
  private static final float NAVIGATION_ZOOM_OFFSET = 0.1f;

  private static final Vector3f tempVec = new Vector3f();
  private List<Bubble> bubbles;
  private List<Link> links;

 // FontTT fontTT;
  private Texture texture;
  private float linkThresholdOffset = Bubbles.MIN_LINK_WIDTH;

  private final Collection<ResearchPaper> papers;

  public Main(Collection<ResearchPaper> papers) {
    this.papers = papers;
  }

  @Override
  public void onGlContextInitialized() throws IOException {
    System.out.println("Started initializing OpenGL...");
    long startTime = System.currentTimeMillis();
    links = new ArrayList<>();
    bubbles = Bubbles.toBubbles(papers, links);
    shuffle(false);
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
    System.out.println("Use S to shuffle the bubbles");
    System.out.println("Use ESCAPE to exit");
  }
// TODO rewrite TextureLoader and test fonTTTZ OR create one texture for each FOS
  @Override
  public void onDraw(Matrix4f projection) {
    texture.bind();
    texture.draw();

    links.forEach(l -> l.draw(projection));
    bubbles.forEach(b -> b.draw(projection));
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
      camera.zoomBy(NAVIGATION_ZOOM_OFFSET);
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
      camera.zoomBy(- NAVIGATION_ZOOM_OFFSET);
    } else {
      camera.addPosition(tempVec.set(0, NAVIGATION_OFFSET, 0));
    }
  }

  private void moveLinkThreshold(float offset) {
    if (linkThresholdOffset <= Bubbles.MIN_LINK_WIDTH && offset < 0 ||
    linkThresholdOffset >= Bubbles.MAX_LINK_WIDTH && offset > 0) {
      return;
    }
    linkThresholdOffset += offset;
    for (Link l : links) {
      l.setVisible(l.getWidth() >= linkThresholdOffset);
    }
    System.out.format("new link threshold: %f.1\n", linkThresholdOffset);
  }

  @Override
  public void onKeyClicked(char c) {
    switch (c) {
      case 'e':
        moveLinkThreshold(LINK_THRESHOLD_OFFSET);
        break;
      case 'd':
        moveLinkThreshold(- LINK_THRESHOLD_OFFSET);
        break;
      case 's': // shuffle
        shuffle(true);
        break;

    }
  }

  @Override
  public void update(float delta) {
    if (isOneAnimationRunning()) {
      links.forEach(Link::update);
    }
  }

  private void shuffle(boolean withAnimation) {
    if (withAnimation) {
      clearAnimations();
      BubblesArranger.arrangeWithAnimation(bubbles, this::addAnimation);
    } else {
      BubblesArranger.arrange(bubbles);
      links.forEach(Link::update);
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
      String s = scanner.nextLine();
      if (s.equals("all")) {
        papers = data.getAllPapers();
      } else {
        papers = data.getPapersByYear().getOrDefault(Integer.parseInt(s), List.of());
      }
      new Main(papers).run();
    }
  }

}