package com.tambapps.papernet.gl.shape;

import lombok.Data;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;


// 0, 0 is center
@Data
public class Rectangle {

  private float x;
  private float y;
  private float width = 0.1f;
  private float height = 0.2f;

  public void draw() {
    glBegin(GL_TRIANGLES);
      glVertex3f(x - width, y + -height, 0.0f);
      glVertex3f(x + width, y - height, 0.0f);
      glVertex3f(x + width, y + height, 0.0f);
      glVertex3f(x - width, y + height, 0.0f);

    glEnd();
  }
}
