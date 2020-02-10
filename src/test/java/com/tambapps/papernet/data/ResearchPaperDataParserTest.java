package com.tambapps.papernet.data;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ResearchPaperDataParserTest {

  @Test
  public void test() throws IOException {
    ResearchPaperData researchPaperData = ResearchPaperDataParser.parseData();

    assertNotNull(researchPaperData.getFosOccurenceMap());
    assertNotNull(researchPaperData.getPaperById());
    assertNotNull(researchPaperData.getPapersByYear());
  }
}
