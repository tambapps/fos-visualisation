package com.tambapps.papernet.visualisation.animation;

import com.tambapps.papernet.gl.shader.ColorShader;

public class ShaderAlphaAnimation extends Animation {

  private final ColorShader shader;
  private final float endAlpha;

  public ShaderAlphaAnimation(ColorShader shader, float duration, float endAlpha) {
    this.shader = shader;
    this.endAlpha = endAlpha;
    setDuration(duration);
  }

  @Override
  protected void update(float percent) {
    shader.setAlpha(percent * endAlpha);
  }
}
