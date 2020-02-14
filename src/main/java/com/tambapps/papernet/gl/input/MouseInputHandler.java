package com.tambapps.papernet.gl.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseInputHandler extends GLFWCursorPosCallback {

  @Override
  public void invoke(long window, double xpos, double ypos) {
    System.out.format("(%f.1, %f.1)", xpos, ypos).println();
  }
}
