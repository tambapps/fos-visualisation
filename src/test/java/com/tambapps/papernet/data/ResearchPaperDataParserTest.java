package com.tambapps.papernet.data;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ResearchPaperDataParserTest {

  @Test
  public void test() throws IOException {
    long startTime = System.currentTimeMillis();
    ResearchPaperData researchPaperData = ResearchPaperDataParser.parseData();
    assertNotNull(researchPaperData.getFosOccurenceMap());
    assertNotNull(researchPaperData.getPaperById());
    assertNotNull(researchPaperData.getPapersByYear());

    System.out.format("It took %dms to load data", System.currentTimeMillis() - startTime).println();
    System.out.format("There is %d articles", researchPaperData.getPaperById().size()).println();
    System.out.format("%d FOS were found", researchPaperData.getFosOccurenceMap().size()).println();
  }
}
