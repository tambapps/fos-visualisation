package com.tambapps.papernet.visualisation.drawable.arranger;

import static com.tambapps.papernet.gl.GlWindow.WINDOW_HEIGHT;
import static com.tambapps.papernet.gl.GlWindow.WINDOW_WIDTH;

import com.tambapps.papernet.visualisation.drawable.Bubble;
import org.joml.Vector3f;

class PositionArranger {

  private static final int TRIES_LIMIT = 10;
  private int boundX = WINDOW_WIDTH;
  private int tries;


  public void init() {
    tries = 0;
  }

  public Vector3f arrange(Bubble bubble, Vector3f vector) {
    try {
      return vector.set(randomX(), randomY(), bubble.getRadius());
    } finally {
      tries++;
      if (tries > TRIES_LIMIT) {
        tries = 0;
        boundX += WINDOW_WIDTH / 10;
      }
    }
  }

  float randomX() {
    return (float) Math.random() * boundX - (boundX >> 1);
  }

  float randomY() {
    return (float) Math.random() * WINDOW_HEIGHT - (WINDOW_HEIGHT >> 1);
  }

  public int getTries() {
    return tries;
  }
}
