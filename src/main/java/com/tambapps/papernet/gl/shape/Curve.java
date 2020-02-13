package com.tambapps.papernet.gl.shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Curve {

  private static final int NB_SEGMENTS = 10;

  private final RectLine tempRectLine = new RectLine();

  float x1;
  float y1;
  float cx1;
  float cy1;
  float cx2;
  float cy2;
  float x2;
  float y2;
  float width;

  public Curve(float width) {
    this.width = width;
  }

  public void draw() {
    GL11.glBegin(GL_TRIANGLES);

    int segments  = NB_SEGMENTS;

    float subdiv_step = 1f / segments;
    float subdiv_step2 = subdiv_step * subdiv_step;
    float subdiv_step3 = subdiv_step * subdiv_step * subdiv_step;

    float pre1 = 3 * subdiv_step;
    float pre2 = 3 * subdiv_step2;
    float pre4 = 6 * subdiv_step2;
    float pre5 = 6 * subdiv_step3;

    float tmp1x = x1 - cx1 * 2 + cx2;
    float tmp1y = y1 - cy1 * 2 + cy2;

    float tmp2x = (cx1 - cx2) * 3 - x1 + x2;
    float tmp2y = (cy1 - cy2) * 3 - y1 + y2;

    float fx = x1;
    float fy = y1;

    float dfx = (cx1 - x1) * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
    float dfy = (cy1 - y1) * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;

    float ddfx = tmp1x * pre4 + tmp2x * pre5;
    float ddfy = tmp1y * pre4 + tmp2y * pre5;

    float dddfx = tmp2x * pre5;
    float dddfy = tmp2y * pre5;

    while (segments-- > 0) {
      float x1 = fx;
      float y1 = fy;
      fx += dfx;
      fy += dfy;
      dfx += ddfx;
      dfy += ddfy;
      ddfx += dddfx;
      ddfy += dddfy;

      float x2 = fx;
      float y2 = fy;
      drawRectLine(x1, y1, x2, y2);
    }
    drawRectLine(fx, fy, x2, y2);

    GL11.glEnd();
  }

  private void drawRectLine(float x1, float y1, float x2, float y2) {
    tempRectLine.setX1(x1);
    tempRectLine.setY1(y1);
    tempRectLine.setX2(x2);
    tempRectLine.setY2(y2);
    tempRectLine.setWidth(width);
    tempRectLine.draw();
  }
}
