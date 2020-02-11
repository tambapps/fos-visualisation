package com.tambapps.papernet;

import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.gl.GlWindow;

import com.tambapps.papernet.gl.texture.Texture;
import com.tambapps.papernet.visualisation.Bubble;
import com.tambapps.papernet.visualisation.Bubbles;
import com.tambapps.papernet.visualisation.Link;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

  @Override
  public void onGlContextInitialized() throws IOException {
    System.out.println("Started loading...");
    long startTime = System.currentTimeMillis();
    Collection<ResearchPaper> papers = ResearchPaperDataParser.parseData(10).getAllPapers();
    links = new ArrayList<>();
    bubbles = Bubbles.toBubbles(papers, links);
    for (Bubble bubble : bubbles) {
      bubble.setX((float)Math.random() * WINDOW_WIDTH - (WINDOW_WIDTH >> 1));
      bubble.setY((float)Math.random() * WINDOW_HEIGHT - (WINDOW_HEIGHT >> 1));
    }
    texture = Texture.newTexture("background.jpg");
    try {
   //   fontTT = new FontTT(Font.createFont(Font.TRUETYPE_FONT, new File("/home/nelson/workspace/graphisme-visualisation-openGL/kenvector_future.ttf")), 16, 0);
    } catch (Exception e) {
      throw new IOException(e);
    }
    System.out.format("Finished loading data (in %ds)\n", (System.currentTimeMillis() - startTime) / 1000L);
    System.out.println("Use the arrow keys to move on the screen");
    System.out.println("pressed left CTRL with up/down to modify the threshold of links");
    System.out.println("Use Z/S to zoom/dezoom from the screen");
  }
// TODO rewrite TextureLoader and test fonTTTZ
  @Override
  public void onDraw(Matrix4f projection) {
    texture.bind();
    texture.draw();

    links.forEach(l -> l.updateNDraw(projection));
    bubbles.forEach(b -> b.draw(projection));
    //   fontTT.drawText("caca", 0.1f, 0, 0, 0, Color.white, 0, 0, 0, false);
  }

  @Override
  public void onLeftPressed() {
    camera.addPosition(tempVec.set(NAVIGATION_OFFSET, 0, 0));
  }

  @Override
  public void onRightPressed() {
    camera.addPosition(tempVec.set(- NAVIGATION_OFFSET, 0, 0));
  }

  @Override
  public void onUpPressed() {
    camera.addPosition(tempVec.set(0, - NAVIGATION_OFFSET, 0));
  }

  @Override
  public void onDownPressed() {
    camera.addPosition(tempVec.set(0, NAVIGATION_OFFSET, 0));
  }

  @Override
  public void onUpCtrlPressed() {
    moveLinkThreshold(LINK_THRESHOLD_OFFSET);
  }

  @Override
  public void onDownCtrlPressed() {
    moveLinkThreshold(- LINK_THRESHOLD_OFFSET);
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
  public void onKeyPressed(char c) {
    switch (c) {
      case 'z':
        camera.zoomBy(NAVIGATION_ZOOM_OFFSET);
        break;
      case 's':
        camera.zoomBy(- NAVIGATION_ZOOM_OFFSET);
    }
  }

  public static void main(String[] args) {
    new Main().run();
  }

}