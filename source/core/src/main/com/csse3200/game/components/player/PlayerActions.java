package com.csse3200.game.components.player;

import java.util.Vector;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.tractor.KeyboardTractorInputComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

import java.io.FileNotFoundException;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_WALK_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final Vector2 MAX_RUN_SPEED = new Vector2(5f, 5f); // Metres per second
  private float prevMoveDirection = 300; // Initialize it with a default value
  private Entity tractor;

  private PhysicsComponent physicsComponent;
  private Vector2 moveDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private boolean running = false;
  private boolean muted = false;
  private CameraComponent camera;
  private GameMap map;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("move", this::move);
    entity.getEvents().addListener("moveStop", this::stopMoving);
    entity.getEvents().addListener("run", this::run);
    entity.getEvents().addListener("runStop", this::stopRunning);
    entity.getEvents().addListener("interact", this::interact);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("enterTractor", this::enterTractor);
    entity.getEvents().addListener("use", this::use);
    entity.getEvents().addListener("hotkeySelection", this::hotkeySelection);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();

    }
    updateAnimation();
  }

  /**
   * Plays the correct animation for the type of player movement.
   */
  private void updateAnimation() {

    if (moveDirection.epsilonEquals(Vector2.Zero)) {
      // player is not moving

      String animationName = "animationWalkStop";
      float direction = getPrevMoveDirection();
      if (direction < 45) {
        entity.getEvents().trigger(animationName, "right");
      } else if (direction < 135) {
        entity.getEvents().trigger(animationName, "up");
      } else if (direction < 225) {
        entity.getEvents().trigger(animationName, "left");
      } else if (direction < 315) {
        entity.getEvents().trigger(animationName, "down");
      }
      return;
    }

    // player is moving
    String animationName = String.format("animation%sStart", running ? "Run" : "Walk");
    float direction = moveDirection.angleDeg();

    if (direction < 45) {
      entity.getEvents().trigger(animationName, "right");
    } else if (direction < 135) {
      entity.getEvents().trigger(animationName, "up");
    } else if (direction < 225) {
      entity.getEvents().trigger(animationName, "left");
    } else if (direction < 315) {
      entity.getEvents().trigger(animationName, "down");
    }
  }

  private Vector2 TileAffectedSpeed(Vector2 runSpeed) {
    // player position
    /*Vector2 playerPos = entity.getPosition();
    playerPos.x = Math.round(playerPos.x);
    playerPos.y = Math.round(playerPos.y);
    if (this.moveDirection.angleDeg() < 45 && map.getTile((int)playerPos.x +1, (int)playerPos.y) != null && !map.getTile((int)playerPos.x +1, (int)playerPos.y).isTraversable()) {
      return new Vector2(0, runSpeed.y);
    } else if (this.moveDirection.angleDeg() < 135 && map.getTile((int)playerPos.x +1, (int)playerPos.y) != null && !map.getTile((int)playerPos.x, (int)playerPos.y +1).isTraversable()) {
      return new Vector2(runSpeed.x, 0);
    } else if (this.moveDirection.angleDeg() < 225 && map.getTile((int)playerPos.x +1, (int)playerPos.y) != null && !map.getTile((int)playerPos.x -1, (int)playerPos.y).isTraversable()) {
      return new Vector2(0, runSpeed.y);
    } else if (this.moveDirection.angleDeg() < 315 && map.getTile((int)playerPos.x +1, (int)playerPos.y) != null && !map.getTile((int)playerPos.x, (int)playerPos.y -1).isTraversable()) {
      return new Vector2(runSpeed.x, 0);
    }*/
    // the getTile is not working as expected
    return runSpeed;
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 velocityScale = this.running ? MAX_RUN_SPEED.cpy() : MAX_WALK_SPEED.cpy();

    //float terrainSpeedModifier = map.getTileSpeedModifier((int) this.entity.getPosition().x, (int) this.entity.getPosition().y);

    if (this.running == true) {
      System.out.println("Vector coordiantes: x:" + String.format("%.2f", entity.getPosition().x) + " y:" + String.format("%.2f", entity.getPosition().y));
      //try {System.out.println();} catch (Exception e) { }
      //System.out.println("TileCoords: " + map.pixelPositionToWorldCoordinates((int) this.entity.getPosition().x, (int) this.entity.getPosition().y));
    }

    //velocityScale.x = velocityScale.x * terrainSpeedModifier;
    //velocityScale.y = velocityScale.y * terrainSpeedModifier;

    // Can simply apply a scalar multiplier effect (maybe .scl) instead of what I did above
    // Could be done by creating a vector2 where it's x and y are the terrain speed modifiers

    Vector2 desiredVelocity = moveDirection.cpy().scl(velocityScale);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  public float getPrevMoveDirection() {
    return prevMoveDirection;
  }
  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void move(Vector2 direction) {
    // Store the previous move direction
    this.moveDirection = direction;
    this.prevMoveDirection = moveDirection.angleDeg();
    moving = true;
  }
  /**
   * Stops the player from moving.
   */
  void stopMoving() {
    this.moveDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
<<<<<<< HEAD
   * Increases the velocity of the player when they move.
   */
  void run() {
    this.running = true;
  }

  /**
   * Removes the velocity increase of the player.
   */
  void stopRunning() {
    this.running = false;
  }

  void interact() {
    float direction = getPrevMoveDirection();
    if (direction < 45) {
      entity.getEvents().trigger("animationInteract", "right");
    } else if (direction < 135) {
      entity.getEvents().trigger("animationInteract", "up");
    } else if (direction < 225) {
      entity.getEvents().trigger("animationInteract", "left");
    } else if (direction < 315) {
      entity.getEvents().trigger("animationInteract", "down");
    }
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
  }

  /**
   * Sets tractor to the tractor entity, can be used to calculate distances and mute inputs
   * @param tractor
   */
  public void setTractor(Entity tractor) {
    this.tractor = tractor;
  }

  /**
   * Makes the player get into tractor.
   */
  void enterTractor() {
    //check within 4 units of tractor
    if (this.entity.getPosition().dst(tractor.getPosition()) > 4) {
      return;
    }
    this.stopMoving();
    muted = true;
    tractor.getComponent(TractorActions.class).setMuted(false);
    tractor.getComponent(KeyboardTractorInputComponent.class).setWalkDirection(entity.getComponent(KeyboardPlayerInputComponent.class).getWalkDirection());
    this.entity.setPosition(new Vector2(-10,-10));
    camera.setTrackEntity(tractor);
  }

  void use(Vector2 playerPos, Vector2 mousePos, Entity itemInHand) {
    if (itemInHand != null) {
      itemInHand.getComponent(ItemActions.class).use(playerPos, mousePos, itemInHand, map);
    }
  }
  void hotkeySelection(int index) {
    entity.getComponent(InventoryComponent.class).setHeldItem(index);
  }

  /**
   * When in the tractor inputs should be muted, this handles that.
   * @return if the players inputs should be muted
   */
  public boolean isMuted() {
    return muted;
  }

  public void setMuted(boolean muted) {
    this.muted = muted;
  }

  public void setCameraVar (CameraComponent cam) {
    this.camera = cam;
  }

  public void setGameMap(GameMap map) {
    this.map = map;
  }
}
