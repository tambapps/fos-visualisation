package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.gl.shader.Shader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.shape.Curve;
import lombok.AllArgsConstructor;
import org.joml.Matrix4f;

import java.io.IOException;

@AllArgsConstructor
public class Link {

  private final Bubble b1;
  private final Bubble b2;
  private final Curve curve;
  private final Shader curveShader;

  public void update() {
    curve.setX1(b1.getX());
    curve.setY1(b1.getY());
    // TODO set others correctly

    curve.setCx1(b2.getX());
    curve.setCy1(b2.getY());


    curve.setCx2(b2.getX());
    curve.setCy2(b2.getY());


    curve.setX2(b2.getX());
    curve.setY2(b2.getY());

    curve.setX2(b2.getX());
    curve.setY2(b2.getY());
  }

  public void draw(Matrix4f projection) {
    curveShader.bind(projection);
    curve.draw();
  }

  public void updateNDraw(Matrix4f projection) {
    update();
    draw(projection);
  }

  public static Link newLink(Bubble b1, Bubble b2, float width) throws IOException {
    return new Link(b1, b2, new Curve(width), ShaderFactory.rgbShader(0.5f, 0.5f, 0.5f));
  }

  public float getWidth() {
    return curve.getWidth();
  }
}
