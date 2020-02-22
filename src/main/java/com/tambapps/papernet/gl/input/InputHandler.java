package com.tambapps.papernet.gl.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.Map;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class InputHandler extends GLFWKeyCallback {

  private final Map<Integer, Character> KEY_CHARACTER_MAP = Map.ofEntries(
    Map.entry(GLFW_KEY_W, 'z'),
    Map.entry(GLFW_KEY_S, 's'),
    Map.entry(GLFW_KEY_E, 'e'),
    Map.entry(GLFW_KEY_D, 'd'),
    Map.entry(GLFW_KEY_R, 'r'),
    Map.entry(GLFW_KEY_F, 'f'),
    Map.entry(GLFW_KEY_Y, 'y'),
    Map.entry(GLFW_KEY_H, 'h'),
    Map.entry(GLFW_KEY_T, 't')
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

  public void update(Character pressedCharacter) {
    if (upPressed) {
      inputListener.onUpPressed(pressedCharacter);
    }
    if (downPressed) {
      inputListener.onDownPressed(pressedCharacter);
    }
    if (leftPressed) {
      inputListener.onLeftPressed(pressedCharacter);
    }
    if (rightPressed) {
      inputListener.onRightPressed(pressedCharacter);
    }
  }

}
