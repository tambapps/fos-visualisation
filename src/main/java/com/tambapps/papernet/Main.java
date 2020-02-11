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
  private static final Vector3f tempVec = new Vector3f();
  private List<Bubble> bubbles;
  private List<Link> links;

 // FontTT fontTT;
  private Texture texture;

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
    System.out.format("Finished loading objects (in %d s)", (System.currentTimeMillis() - startTime) / 1000L);
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

  public static void main(String[] args) {
    new Main().run();
  }

}