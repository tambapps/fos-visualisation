package com.tambapps.papernet.gl.shape;

import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;


public class Circle {

  private static final float PI = (float) Math.PI;
  private static final int NB_SEGMENTS = 20;

  private float x;
  private float y;
  private float radius = 0.1f;

  public void draw() {
    glBegin(GL_POLYGON);
    float angle = 2 * PI / NB_SEGMENTS;
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);
    float cx = radius;
    float cy = 0;

    for (int i = 0; i < NB_SEGMENTS - 1; i++) {
      glVertex3f(x, y, 0);
      glVertex3f(x + cx, y + cy, 0.0f);
      float temp = cx;
      cx = cos * cx - sin * cy;
      cy = sin * temp + cos * cy;
      glVertex3f(x + cx, y + cy, 0.0f);
    }
    glVertex3f(x, y, 0.0f);
    glVertex3f(x + cx, y + cy, 0.0f);
    cx = radius;
    cy = 0;
    glVertex3f(x + cx, y + cy, 0.0f);
    glEnd();
  }
}
