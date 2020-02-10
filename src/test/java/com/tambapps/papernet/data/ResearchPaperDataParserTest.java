package com.tambapps.papernet.data;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ResearchPaperDataParserTest {

  @Test
  public void test() throws IOException {
    FosData fosData = FosDataParser.parseData();

    assertNotNull(fosData.getFosOccurenceMap());
    assertNotNull(fosData.getFosById());
    assertNotNull(fosData.getFosByYear());
  }
}
