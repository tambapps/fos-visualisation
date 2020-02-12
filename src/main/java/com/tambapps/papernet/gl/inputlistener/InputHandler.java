package com.tambapps.papernet.gl.inputlistener;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.Map;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class InputHandler extends GLFWKeyCallback {

  // TODO OOOO CREATE ANIMATION WHEN SHUFFLING BUBBLES AND INTERPOLATE MOVMENT WITH A SQUARED FUNCTION
  private final Map<Integer, Character> KEY_CHARACTER_MAP = Map.ofEntries(
    Map.entry(GLFW_KEY_W, 'z'),
    Map.entry(GLFW_KEY_S, 's'),
    Map.entry(GLFW_KEY_E, 'e'),
    Map.entry(GLFW_KEY_D, 'd')
  );
  private final InputListener inputListener;
  private boolean upPressed = false;
  private boolean downPressed = false;
  private boolean leftPressed = false;
  private boolean rightPressed = false;

  public InputHandler(InputListener inputListener) {
    this.inputListener = inputListener;
  }



  @Override
  public void invoke(long window, int key, int scancode, int action, int mods) {
    if (action == GLFW_RELEASE) {
      switch (key) {
        case GLFW_KEY_ESCAPE:
          inputListener.onEscapePressed();
          break;
        case GLFW_KEY_LEFT:
          leftPressed = false;
          break;
        case GLFW_KEY_RIGHT:
          rightPressed = false;
          break;
        case GLFW_KEY_UP:
          upPressed = false;
          break;
        case GLFW_KEY_DOWN:
          downPressed = false;
          break;
        default:
          inputListener.onKeyClicked(KEY_CHARACTER_MAP.getOrDefault(key, ' ')); // to avoid NPO
      }
    } else {
      switch (key) {
        case GLFW_KEY_LEFT:
          leftPressed = true;
          break;
        case GLFW_KEY_RIGHT:
          rightPressed = true;
          break;
        case GLFW_KEY_UP:
          upPressed = true;
          break;
        case GLFW_KEY_DOWN:
          downPressed = true;
          break;
      }
    }
  }

  public void update(boolean ctrlPressed) {
    if (upPressed) {
      inputListener.onUpPressed(ctrlPressed);
    }
    if (downPressed) {
      inputListener.onDownPressed(ctrlPressed);
    }
    if (leftPressed) {
      inputListener.onLeftPressed(ctrlPressed);
    }
    if (rightPressed) {
      inputListener.onRightPressed(ctrlPressed);
    }
  }

}
