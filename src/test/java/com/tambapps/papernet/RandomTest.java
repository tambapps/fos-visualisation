package com.tambapps.papernet;

import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.visualisation.drawable.Bubble;
import com.tambapps.papernet.visualisation.drawable.Bubbles;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class RandomTest {

  @Test
  public void randomTest() throws IOException {
    Collection<ResearchPaper> papers = ResearchPaperDataParser.parseData(10).getAllPapers();
    List<Bubble> bubbles = Bubbles.toBubbles(papers, null);
    System.out.println(bubbles);
  }
}
