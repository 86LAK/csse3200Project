package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.Arrays;

/*
 * Current functionality:
 * - when a sprinkler is placed next to a power source it will water its AOE and act as a power source for any
 *   other sprinklers that are placed next to it in future.
 *
 * TODO:
 *  - get sprinklers to update each other (on place and destroy).
 *    - use dispose() i think.
 *  - get sprinklers to sprinkle ever 5 seconds or something.
 *  - make and set textures accordingly.
 *  - animate watering.
 */

public class SprinklerComponent extends Component {
  /**
   * Powered status of sprinkler.
   */
  private boolean isPowered;

  /**
   * Indicates if the 'sprinkler' is actually just a pump.
   * A pump is a sprinkler that doesn't update or sprinkler but is powered.
   */
  private boolean pump;

  /**
   * A sprinklers area of effect to water, aoe is circular with radius of 2.
   */
  private Vector2[] aoe;

  /**
   * An ordered list of adjacent sprinklers or pumps,
   * if an adjacent tile does not hava a sprinkler/pump, null is placed in that index.
   * The order is: [ Above, Below, Right, Left ].
   */
  private Entity[] adjSprinklers;


  public void create() {
    // If this is a pump we just need to turn its power on:
    if (this.pump) {
      this.isPowered = true;
      return;
    }
    // Sprinklers need configuring:
    setAoe();
    getAdjSprinklers();
    configSprinkler(); //TODO: unfinished method
    //notifyAdj(); // TODO: unfinished *dangerous* method

    if (this.isPowered) {
      sprinkle();
    }

    // TODO: [DEBUGGING]
    System.out.println("adj sprinklers: "+ Arrays.toString(this.adjSprinklers));
  }

  /**
   * sprinklerUpdate is called by an adjacent sprinkler, causing this sprinkler to
   * re-configure its adjacent sprinklers and power status.
   * Pumps do not need to be updated.
   */
  public void sprinklerUpdate() {
    if (!pump) {
      getAdjSprinklers();
      configSprinkler();
      //notifyAdj(); // TODO: careful
    }
  }

  /**
   * notifies adjacent sprinklers to call sprinklerUpdate.
   * TODO: haven't thought this through probably cause recursion bomb.
   */
  private void notifyAdj() {
    for (Entity sprinkler : this.adjSprinklers) {
      if (sprinkler != null) {
        sprinkler.getComponent(SprinklerComponent.class).sprinklerUpdate();
      }
    }
  }

  /**
   * Allows other classes to query if this sprinkler has power.
   * @return powered status
   */
  public boolean getPowered() {
    return isPowered;
  }

  /**
   * Sets this sprinkler to be a pump.
   * Should only be called in PlaceableFactory.
   */
  public void setPump() {
    this.pump = true;
  }

  /**
   * Sets the coordinates for the watering area-of-effect.
   * Coordinates are this sprinklers position +2 in all directions, +1 in diagonals.
   */
  private void setAoe() {
    // if there is a smarter way to get these co-ords im listening.
    float x = entity.getPosition().x, y = entity.getPosition().y;
    this.aoe = new Vector2[] {
            // 2up, 2down, 2right, 2left.
            new Vector2(x, y+1),
            new Vector2(x, y+2),
            new Vector2(x, y-1),
            new Vector2(x, y-2),
            new Vector2(x+1, y),
            new Vector2(x+2, y),
            new Vector2(x-1, y),
            new Vector2(x-2, y),
            // top right, bottom right, top left, bottom left.
            new Vector2(x+1, y+1),
            new Vector2(x+1, y-1),
            new Vector2(x-1, y+1),
            new Vector2(x-1, y-1)
    };
  }

  /**
   * Gets directly adjacent sprinklers excluding diagonals,
   * after this call, each index in this.adjSprinklers will either be a sprinkler/pump entity or null.
   */
  private void getAdjSprinklers() {
    final int total = 4;
    this.adjSprinklers = new Entity[total];
    for (int i = 0; i < total; i++) {
      int dir = i * 2;  // Map adjSprinkler index to aoe index.
      Entity p = ServiceLocator.getGameArea().getMap().getTile(aoe[dir]).getPlaceable();
      if (p != null) {
        if (p.getComponent(SprinklerComponent.class) == null) {
          p = null;
        }
      }
      this.adjSprinklers[i] = p;
    }
  }

  /**
   * Sets powered status and texture 'orientation' by scanning the adjacent sprinklers/pump.
   *  - A power source is either a pump or a powered sprinkler.
   *  - A texture is selected for this sprinkler based on the surrounding sprinklers,
   *    this illustrates to the player that these sprinklers are connected - like pipes.
   *    - Available textures are:
   *        * straight horizontal
   *        * straight vertical
   *        * 90 degree bend in any orientation
   *        * 3 way connection in any orientation
   *        * 4 way connection
   */
  public void configSprinkler() {
    // TODO: add texture selection functionality

    for (Entity sprinkler : this.adjSprinklers) {
      if (sprinkler != null) {
        // Sets this sprinkler to powered if adj sprinkler is powered, but never sets to this.isPowered to false.
        this.isPowered = sprinkler.getComponent(SprinklerComponent.class).getPowered() || this.isPowered;
      }
    }
  }

  /**
   * Waters the aoe, this is relevant to this sprinklers position and looks like:
   * * 2 tiles: above, below, left, right.
   * * 1 tile in each diagonal.
   * Creating a circular watering effect.
   */
  private void sprinkle() {
    for (Vector2 pos : aoe) {
      TerrainTile tt = ServiceLocator.getGameArea().getMap().getTile(pos);
      if (tt.getCropTile() != null) {
        tt.getCropTile().getEvents().trigger("water", 1f);
      }
    }
  }
}