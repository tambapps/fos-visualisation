package com.tambapps.papernet.gl.input;

public interface InputListener {

  void onLeftPressed(Character ctrlPressed);

  void onRightPressed(Character ctrlPressed);

  void onUpPressed(Character ctrlPressed);

  void onDownPressed(Character ctrlPressed);

  void onKeyClicked(char c);

  void onEscapePressed();
}
