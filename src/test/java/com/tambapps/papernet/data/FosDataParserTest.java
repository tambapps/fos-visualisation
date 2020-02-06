package com.tambapps.papernet.data;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FosDataParserTest {

  @Test
  public void test() throws IOException {
    FosData fosData = FosDataParser.parseData();

    assertNotNull(fosData.getFosOccurenceMap());
    assertEquals(18901, fosData.getFosOccurenceMap().size());
  }
}
