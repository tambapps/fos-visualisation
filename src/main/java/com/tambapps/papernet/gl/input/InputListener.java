package com.tambapps.papernet.gl.input;

public interface InputListener {

  void onLeftPressed(boolean ctrlPressed);
  void onRightPressed(boolean ctrlPressed);
  void onUpPressed(boolean ctrlPressed);
  void onDownPressed(boolean ctrlPressed);
  void onKeyClicked(char c);

  void onEscapePressed();
}
