package com.tambapps.papernet.gl.view;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

  private final Vector3f position;
  private Matrix4f projection;
  private float zoom = 1f;


  public Camera(float width, float height) {
    position = new Vector3f(0, 0, 0);
    projection = new Matrix4f().setOrtho2D(- width / 2, width / 2,
      - height / 2, height / 2);
  }

  public void setPosition(Vector3f position) {
    this.position.set(position);
  }

  public void addPosition(Vector3f position) {
    this.position.add(position);
  }

  public void setZoom(float zoom) {
    this.zoom = zoom;
  }

  public Matrix4f getProjection() {
    Matrix4f target = new Matrix4f();
    Matrix4f pos = new Matrix4f().setTranslation(position);
    target = projection.mul(pos, target);
    target.scale(zoom);
    return target;
  }


  public void zoomBy(float by) {
    if (this.zoom <= 0 && by < 0 || this.zoom >= 5f && by > 0) {
      return;
    }
    this.zoom += by;
  }
}
