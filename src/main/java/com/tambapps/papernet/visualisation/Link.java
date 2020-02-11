package com.tambapps.papernet.visualisation;

import com.tambapps.papernet.gl.shape.Curve;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Link {
  private Bubble b1;
  private Bubble b2;
  private Curve curve;

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

  public void draw() {
    curve.draw();
  }

  public void updateNDraw() {
    update();
    draw();
  }

  public static Link newLink(Bubble b1, Bubble b2, float width) {
    return new Link(b1, b2, new Curve(width));
  }
}
