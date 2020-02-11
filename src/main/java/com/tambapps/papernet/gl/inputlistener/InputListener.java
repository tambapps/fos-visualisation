package com.tambapps.papernet.gl.inputlistener;

public interface InputListener {

  void onLeftPressed();
  void onRightPressed();
  void onUpPressed();
  void onUpCtrlPressed();
  void onDownPressed();
  void onDownCtrlPressed();
  void onKeyPressed(char c);

}
