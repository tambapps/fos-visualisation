package com.tambapps.papernet;

import com.tambapps.papernet.data.FosDataParser;
import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.visualisation.Bubble;
import com.tambapps.papernet.visualisation.Bubbles;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class RandomTest {

  @Test
  public void randomTest() throws IOException {
    Collection<ResearchPaper> papers = FosDataParser.parseData(10).getAllPapers();
    List<Bubble> bubbles = Bubbles.toBubbles(papers);
    System.out.println(bubbles);
  }
}
