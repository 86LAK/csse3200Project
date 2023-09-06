package com.csse3200.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea implements Disposable {
  protected TerrainComponent terrain;
  protected List<Entity> areaEntities;

  protected GameArea() {
    areaEntities = new ArrayList<>();
  }

  /** Create the game area in the world. */
  public abstract void create();

  /** Dispose of all internal entities in the area */
  public void dispose() {
    for (Entity entity : areaEntities) {
      entity.dispose();
    }
  }

  /**
   * Spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  public void spawnEntity(Entity entity) {
    areaEntities.add(entity);
    ServiceLocator.getEntityService().register(entity);
  }

  /**
   * de-spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  protected void deSpawnEntity(Entity entity) {
    areaEntities.remove(entity);
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   *
   * @param entity Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param centerX true to center entity X on the tile, false to align the bottom left corner
   * @param centerY true to center entity Y on the tile, false to align the bottom left corner
   */
  public void spawnEntityAt(
      Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
    float tileSize = terrain.getTileSize();

    if (centerX) {
      worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
    }
    if (centerY) {
      worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
    }

    entity.setPosition(worldPos);
    spawnEntity(entity);
  }


  
  public void removeEntity(Entity entity) {
    entity.setEnabled(false);
    areaEntities.remove(entity);
    Gdx.app.postRunnable(entity::dispose); 
  }

  /**
   * Loops through the games npcs and removes them. REmoves all
   * cows, chickens and Astrolotl from the game
   * @param npcs Array<entity> containing all chickens, cows and Astrolotl to be removed
   */
  public void removeNPCs(Array<Entity> npcs) {
    for (Entity npc : npcs) {
      if (npc.getType() == EntityType.Cow || npc.getType() == EntityType.Chicken ||
          npc.getType() == EntityType.Astrolotl) {
        removeEntity(npc);
      }
    }
  }



  
  public abstract Entity getPlayer();

  public abstract Entity getTractor();

  public abstract GameMap getMap();
}
