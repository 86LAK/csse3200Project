package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {
  
  /**
   * Creates an invisible obstacle entity which located onto the non-traversable area of the map.
   * @return Invisible obstacle entity
   */
  public static Entity createInvisibleObstacle() {
    Entity obstacle =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/invisible_sprite.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    obstacle.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    obstacle.getComponent(TextureRenderComponent.class).scaleEntity();
    obstacle.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(obstacle, 1f, 1f);
    return obstacle;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
