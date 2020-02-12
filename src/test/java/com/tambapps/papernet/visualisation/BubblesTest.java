package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.visualisation.drawable.Bubbles;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class BubblesTest {

  @Test
  public void percentageTest() {
    assertEquals(0f, Bubbles.toPercentage(12, 12,  100));
    assertEquals(1f, Bubbles.toPercentage(100, 12,  100));

    assertEquals(0.5f, Bubbles.toPercentage(50, 0,  100));
  }
}
