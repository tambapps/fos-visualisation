package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.visualisation.drawable.Bubble;
import com.tambapps.papernet.visualisation.drawable.FosNet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FosNetTest extends GlTest {

  private static final ResearchPaperData DATA;
  static {
    try {
      DATA = ResearchPaperDataParser.parseData();
    } catch (IOException e) {
      throw new RuntimeException("Couldn't start tests", e);
    }
  }

  private final FosNet fosNet = new FosNet(DATA);

  @Test
  public void setYearTest() {
    fosNet.loadYear(2000, false);
    List<Bubble> yearBubbles = List.copyOf(fosNet.getBubbles());
    assertFalse(yearBubbles.isEmpty());
    fosNet.loadYear(0, false);
    fosNet.loadYear(2000, false);
    assertEquals(yearBubbles, fosNet.getBubbles());
  }
}
