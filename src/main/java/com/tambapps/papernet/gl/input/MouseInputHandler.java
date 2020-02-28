package com.tambapps.papernet.gl.input;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import com.tambapps.papernet.gl.GlWindow;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseInputHandler {

  private static final int GL_ACTION_TOUCH_DOWN = 1;
  private static final int GL_ACTION_TOUCH_UP = 0;
  private final MouseListener mouseListener;
  private boolean clicked = false;
  private float x;
  private float y;

  public MouseInputHandler(MouseListener mouseListener) {
    this.mouseListener = mouseListener;
  }

  public void addInput(long window) {
    glfwSetMouseButtonCallback(window, new ClickCallBack());
    glfwSetCursorPosCallback(window, new CursorCallBack());
  }

  private class ClickCallBack extends GLFWMouseButtonCallback {
    @Override
    public void invoke(long window, int button, int action, int mods) {
      switch (action) {
        case GL_ACTION_TOUCH_DOWN:
          clicked = true;
          mouseListener.onTouchDown(x, y);
          break;
        case GL_ACTION_TOUCH_UP:
          clicked = false;
          mouseListener.onTouchUp();
          break;
      }
    }
  }


  private class CursorCallBack extends GLFWCursorPosCallback {

    @Override
    public void invoke(long window, double xpos, double ypos) {
      x = ((float) xpos) - (GlWindow.WINDOW_WIDTH >> 1);
      y = -(((float) ypos) - (GlWindow.WINDOW_HEIGHT >> 1));
      if (!clicked) {
        return;
      }
      mouseListener.onMouseDragged(x, y);
    }
  }
}
