package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.gl.shader.ColorShader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Curve;
import lombok.AllArgsConstructor;
import org.joml.Matrix4f;

import java.io.IOException;

@AllArgsConstructor
public class Link extends AbstractDrawable {

  public static final float MAX_ALPHA = 0.6f;

  private final Bubble b1;
  private final Bubble b2;
  private final Curve curve;
  private final ColorShader curveShader;

  public void update() {
    setVisible(b1.isVisible() && b2.isVisible());
    if (!isVisible()) {
      return;
    }
    updatePos();
  }
  public void updatePos() {
    curve.setX1(b1.getX());
    curve.setY1(b1.getY());

    float xOffset = b2.getX() - b1.getX();
    float yOffset = b2.getY() - b1.getY();

    // right up
    curve.setCx1(b1.getX() + xOffset * 0.6f);
    curve.setCy1(b1.getY() + yOffset * 0.2f);

    curve.setCx2(b1.getX() + xOffset * 0.9f);
    curve.setCy2(b1.getY() + yOffset * 0.4f);

    curve.setX2(b2.getX());
    curve.setY2(b2.getY());
  }

  @Override
  public void setVisible(boolean visible) {
    if (!isVisible() && (!b1.isVisible() || !b2.isVisible())) {
      return;
    }
    super.setVisible(visible);
  }

  @Override
  protected void doDraw(Matrix4f projection) {
    curveShader.bind(projection);
    curve.draw();
  }

  @Override
  public void setAlpha(float alpha) {
    curveShader.setAlpha(alpha * MAX_ALPHA);
  }

  @Override
  public float getAlpha() {
    return curveShader.getAlpha();
  }

  public static Link newLink(Bubble b1, Bubble b2, float width) throws IOException {
    return new Link(b1, b2, new Curve(width), ShaderFactory.rgbaShader(0.5f, 0.5f, 0.5f, Link.MAX_ALPHA));
  }

  public float getWidth() {
    return curve.getWidth();
  }
}
