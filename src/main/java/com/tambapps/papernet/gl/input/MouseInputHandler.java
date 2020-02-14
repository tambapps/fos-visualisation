package com.tambapps.papernet.gl.input;

import com.tambapps.papernet.gl.GlWindow;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

public class MouseInputHandler {

  private boolean clicked = false;
  private boolean touchedDown = false;
  private static final int GL_ACTION_TOUCH_DOWN  = 1;
  private static final int GL_ACTION_TOUCH_UP  = 0;

  private final MouseListener mouseListener;

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
      clicked = action == GL_ACTION_TOUCH_DOWN;
      if (action == GL_ACTION_TOUCH_UP) {
        touchedDown = false;
      }
    }
  }

  private class CursorCallBack extends GLFWCursorPosCallback {

    @Override
    public void invoke(long window, double xpos, double ypos) {
      if (!clicked) {
        return;
      }
      float x = ((float) xpos) - (GlWindow.WINDOW_WIDTH >> 1);
      float y = - (((float)ypos) - (GlWindow.WINDOW_HEIGHT >> 1));
      if (!touchedDown) {
        mouseListener.onTouchDown(x, y);
        touchedDown = true;
      } else {
        mouseListener.onMouseDragged(x, y);
      }
    }
  }
}
