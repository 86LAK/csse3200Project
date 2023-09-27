package com.csse3200.game.components.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class TouchAttackComponent extends Component {
  private short targetLayer;
  private float knockbackForce = 0f;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;
  private float stunDuration = 0f;

  /**
   * Create a component which attacks entities on collision, without knockback.
   * @param targetLayer The physics layer of the target's collider.
   */
  public TouchAttackComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  /**
   * Create a component which attacks entities on collision, with knockback.
   * @param targetLayer The physics layer of the target's collider.
   * @param knockback The magnitude of the knockback applied to the entity.
   */
  public TouchAttackComponent(short targetLayer, float knockback) {
    this.targetLayer = targetLayer;
    this.knockbackForce = knockback;
  }

  public TouchAttackComponent(short targetLayer, float knockback, float stunDuration) {
    this(targetLayer, knockback);
    this.stunDuration = stunDuration;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  private void onCollisionStart(Fixture me, Fixture other) {
    if (!enabled) {
      return;
    }

    if (hitboxComponent.getFixture() != me) {
      // Not triggered by hitbox, ignore
      return;
    }

    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
      // Doesn't match our target layer, ignore
      return;
    }

    // Try to attack target.
    Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

    // Apply knockback
    ProjectileComponent projectileComponent = entity.getComponent(ProjectileComponent.class);
    PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
    Vector2 knockBackDirection;
    if (physicsComponent != null && knockbackForce > 0f) {
      if (projectileComponent != null) {
        knockBackDirection = projectileComponent.getVelocity();
      } else {
        knockBackDirection = target.getCenterPosition().sub(entity.getCenterPosition());
      }

      Body targetBody = physicsComponent.getBody();
      targetBody.setLinearVelocity(0,  0);
      Vector2 impulse = knockBackDirection.setLength(knockbackForce);
      targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
    }

    if (entity.getComponent(ProjectileComponent.class) != null) {
      entity.getEvents().trigger("impactStart");
    }

    target.getEvents().trigger("hit", entity);
    target.getEvents().trigger("triggerStunDuration", stunDuration);
  }
}
