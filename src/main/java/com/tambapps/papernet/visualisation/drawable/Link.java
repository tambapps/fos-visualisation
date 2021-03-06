package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.gl.shader.ColorShader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Curve;
import lombok.AllArgsConstructor;
import org.joml.Matrix4f;

@AllArgsConstructor
public class Link extends AbstractDrawable {

  public static final float MAX_ALPHA = 0.6f;

  private Node b1;
  private Node b2;
  private Curve curve;
  private ColorShader curveShader;

  public static Link newLink(Node b1, Node b2, float width) {
    return new Link(b1, b2, new Curve(width), ShaderFactory.linksShader());
  }

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
  public float getAlpha() {
    return curveShader.getAlpha();
  }

  @Override
  public void setAlpha(float alpha) {
    curveShader.setAlpha(alpha * MAX_ALPHA);
  }

  public void init(Node b1, Node b2, float width) {
    this.b1 = b1;
    this.b2 = b2;
    this.curve.setWidth(width);
  }

  public float getWidth() {
    return curve.getWidth();
  }
}
