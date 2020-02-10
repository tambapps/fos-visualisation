package com.tambapps.papernet;

import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.gl.GlWindow;

import com.tambapps.papernet.gl.shape.Curve;
import com.tambapps.papernet.gl.texture.Texture;
import com.tambapps.papernet.visualisation.Bubble;
import com.tambapps.papernet.visualisation.Bubbles;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.glFlush;

// TODO UTILISER TEXTURE EN BACKGROUND
public class Main extends GlWindow {

  List<Bubble> bubbles;

 // FontTT fontTT;
  private Texture texture;
  private Curve curve = new Curve(-1, -1, 0.1f, 0.1f, 0.25f, 0.25f, 1, -1, 4);

  @Override
  public void onGlContextInitialized() throws IOException {
    Collection<ResearchPaper> papers = ResearchPaperDataParser.parseData(10).getAllPapers();
    bubbles = Bubbles.toBubbles(papers);
    for (Bubble bubble : bubbles) {
      bubble.setX((float)Math.random() * 2 - 1);
      bubble.setY((float)Math.random() * 2 - 1);
    }
    texture = Texture.newTexture("background.jpg");
    try {
   //   fontTT = new FontTT(Font.createFont(Font.TRUETYPE_FONT, new File("/home/nelson/workspace/graphisme-visualisation-openGL/kenvector_future.ttf")), 16, 0);
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
// TODO rewrite TextureLoader and test fonTTTZ
  @Override
  public void onDraw() {
    texture.bind();
    texture.draw();

     bubbles.forEach(Bubble::draw);

    curve.draw();
 //   fontTT.drawText("caca", 0.1f, 0, 0, 0, Color.white, 0, 0, 0, false);
  }

  public static void main(String[] args) throws IOException {
    new Main().run();
  }

}