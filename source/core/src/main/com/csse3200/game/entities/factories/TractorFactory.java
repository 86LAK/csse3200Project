package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.tractor.KeyboardTractorInputComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.components.tractor.TractorAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class TractorFactory {

  /**
   * Creates an Entity and adds all necessary componenets to make it function as a
   * tractor should
   *
   * @param player - a reference to the player that should be allowed to enter and
   *               control it
   *
   * @return a referenece to the tractor entity
   */
  public static Entity createTractor(Entity player) {

    AnimationRenderComponent animator = setupTractorAnimations();
    InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForTractor();

    AuraLightComponent light = new AuraLightComponent(3f);

    Entity tractor = new Entity(EntityType.Tractor)
        .addComponent(new PhysicsComponent())
        .addComponent(new TractorAnimationController())
        .addComponent(new ColliderComponent())
        .addComponent(animator)
        .addComponent(inputComponent)
        .addComponent(light)
        .addComponent(new TractorActions());

    tractor.getComponent(AnimationRenderComponent.class).scaleEntity();
    tractor.getComponent(TractorActions.class).setPlayer(player);
    tractor.getComponent(KeyboardTractorInputComponent.class).setActions(tractor.getComponent(TractorActions.class));
    tractor.getComponent(ColliderComponent.class).setAsBox(new Vector2(2.25f, 1f), new Vector2(2.5625f, 1.4375f));
    tractor.getComponent(ColliderComponent.class).setDensity(999);    //prevents entities from pushing the tractor so easily
    return tractor;
  }

  /**
   * Adds all animations to the AnimationRenderComponent for the tractor to use
   *
   * @return an AnimationRenderComponent with the tractors animations added.
   */
  private static AnimationRenderComponent setupTractorAnimations() {
    AnimationRenderComponent animator = new AnimationRenderComponent(
        ServiceLocator.getResourceService().getAsset("images/tractor.atlas", TextureAtlas.class),
        16f);

    animator.addAnimation("idle_left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_up", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_down", 0.1f, Animation.PlayMode.LOOP);

    animator.addAnimation("stop_left_normal", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_right_normal", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_up_normal", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_down_normal", 0.1f, Animation.PlayMode.LOOP);

    animator.addAnimation("move_left_normal", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_right_normal", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_up_normal", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_down_normal", 0.1f, Animation.PlayMode.LOOP);

    animator.addAnimation("stop_left_tilling", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_right_tilling", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_up_tilling", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_down_tilling", 0.1f, Animation.PlayMode.LOOP);

    animator.addAnimation("move_left_tilling", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_right_tilling", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_up_tilling", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_down_tilling", 0.1f, Animation.PlayMode.LOOP);

    animator.addAnimation("stop_left_harvesting", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_right_harvesting", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_up_harvesting", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("stop_down_harvesting", 0.1f, Animation.PlayMode.LOOP);

    animator.addAnimation("move_left_harvesting", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_right_harvesting", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_up_harvesting", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_down_harvesting", 0.1f, Animation.PlayMode.LOOP);

    return animator;
  }
}
