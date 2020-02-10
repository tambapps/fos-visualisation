package com.tambapps.papernet;

import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.gl.GlWindow;

import com.tambapps.papernet.visualisation.Bubble;
import com.tambapps.papernet.visualisation.Bubbles;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

// TODO UTILISER TEXTURE EN BACKGROUND
public class Main extends GlWindow {

  List<Bubble> bubbles;

  @Override
  public void onGlContextInitialized() throws IOException {
    Collection<ResearchPaper> papers = ResearchPaperDataParser.parseData(10).getAllPapers();
    bubbles = Bubbles.toBubbles(papers);
    for (Bubble bubble : bubbles) {
      bubble.setX((float)Math.random());
      bubble.setY((float)Math.random());
    }
  }

  @Override
  public void onDraw() {
    bubbles.forEach(Bubble::draw);
  }

  public static void main(String[] args) throws IOException {
    new Main().run();
  }

}