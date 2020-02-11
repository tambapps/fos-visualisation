package com.tambapps.papernet.gl.shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

@Data
@AllArgsConstructor
public class RectLine {

  private static final Vector2f tempVec = new Vector2f();

  private float x1;
  private float y1;
  private float x2;
  private float y2;
  private float width;

  public void draw() {
    Vector2f t = tempVec.set(y2 - y1, x1 - x2).normalize();
    float width = this.width * 0.5f;
    float tx = t.x * width;
    float ty = t.y * width;
    glBegin(GL_QUADS);


    glVertex3f(x1 + tx, y1 + ty, 0);

    glVertex3f(x1 - tx, y1 - ty, 0);

    glVertex3f(x2 + tx, y2 + ty, 0);


    glVertex3f(x2 - tx, y2 - ty, 0);

    glVertex3f(x2 + tx, y2 + ty, 0);

    glVertex3f(x1 - tx, y1 - ty, 0);

    glEnd();
  }

}
