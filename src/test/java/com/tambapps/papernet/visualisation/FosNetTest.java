package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.visualisation.animation.Animation;
import com.tambapps.papernet.visualisation.drawable.Node;
import com.tambapps.papernet.visualisation.drawable.FosNet;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FosNetTest extends GlTest {

  private static final Consumer<Animation> EMPTY_CONSUMER = animation -> { };
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
    fosNet.loadYear(2000, EMPTY_CONSUMER, 0, 0);
    List<Node> yearNodes = List.copyOf(fosNet.getNodes());
    assertFalse(yearNodes.isEmpty());
    fosNet.loadYear(0, EMPTY_CONSUMER, 0, 0);
    fosNet.loadYear(2000, EMPTY_CONSUMER, 0, 0);
    assertEquals(yearNodes, fosNet.getNodes());
  }
}
