package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;

public class CameraComponent extends Component {
  private final Camera camera;
  private Vector2 lastPosition;
  private Entity trackEntity;

  public CameraComponent() {
    this(new OrthographicCamera());
  }

  public CameraComponent(Camera camera) {
    this.camera = camera;
    lastPosition = Vector2.Zero.cpy();
  }

  @Override
  public void update() {
    Vector2 position;
    if (trackEntity != null) {
      PhysicsComponent physicsComponent = trackEntity.getComponent(PhysicsComponent.class);
      if (physicsComponent != null) {
        position = physicsComponent.getBody().getWorldCenter();
      } else {
        position = trackEntity.getCenterPosition();
      }
    } else {
      position = entity.getPosition();
    }
    if (!lastPosition.epsilonEquals(entity.getPosition())) {
      camera.position.set(position.x, position.y, 0f);
      lastPosition = position;
      camera.update();
    }
  }

  public Matrix4 getProjectionMatrix() {
    return camera.combined;
  }

  public Camera getCamera() {
    return camera;
  }

  public void setTrackEntity(Entity trackEntity) {
    this.trackEntity = trackEntity;
  }

  public Entity getTrackEntity() {
    return trackEntity;
  }

  public void resize(int screenWidth, int screenHeight, float gameWidth) {
    float ratio = (float) screenHeight / screenWidth;
    camera.viewportWidth = gameWidth;
    camera.viewportHeight = gameWidth * ratio;
    camera.update();
  }
}
