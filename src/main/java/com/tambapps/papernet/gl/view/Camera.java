package com.tambapps.papernet.gl.view;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

  private static final Vector3f tempVec = new Vector3f();

  private final float width;
  private final float height;
  private final Vector3f position;
  private Matrix4f projection;
  private float zoom = 1f;


  public Camera(float width, float height) {
    this.width = width;
    this.height = height;
    position = new Vector3f(0, 0, 0);
    update();
  }

  private void update() {
    projection = new Matrix4f().setOrtho2D(zoom * - width / 2, zoom * width / 2,
      zoom * - height / 2, zoom * height / 2);
  }

  public void setPosition(Vector3f position) {
    this.position.set(position);
  }

  public void addPosition(Vector3f position) {
    this.position.add(position.mul(zoom));
  }

  public Matrix4f getProjection() {
    Matrix4f target = new Matrix4f();
    Matrix4f pos = new Matrix4f().setTranslation(position);
    target = projection.mul(pos, target);
    return target;
  }


  public void zoomBy(float by) {
    if (this.zoom <= 0.1f && by < 0 || this.zoom >= 2f && by > 0) {
      return;
    }
    this.zoom += by;
    update();
  }

  public Vector3f getPosition() {
    return position;
  }

  public float getZoom() {
    return zoom;
  }

  public Vector3f projectPoint(float x, float y) {
    float zoom = getZoom();
    Vector3f cameraPos = getPosition();
    return tempVec.set((x - cameraPos.x) * zoom, (y - cameraPos.y) * zoom, 0);
  }

}
