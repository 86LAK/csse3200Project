package com.csse3200.game.areas;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class GameAreaTest {
  @Test
  void shouldSpawnEntities() {
    TerrainFactory factory = mock(TerrainFactory.class);

    GameArea gameArea =
        new GameArea() {
          @Override
          public void create() {}

            /**
             * @return Returns the player in the game area
             */
            @Override
            public Entity getPlayer() {
                return null;
            }
            @Override
            public ClimateController getClimateController() {
                return null;
            }

            @Override
            public Entity getTractor() {
                return null;
            }

            @Override
            public GameMap getMap() {
                return null;
            }
        };

    ServiceLocator.registerEntityService(new EntityService());
    Entity entity = mock(Entity.class);

    gameArea.spawnEntity(entity);
    verify(entity).create();

    gameArea.dispose();
    verify(entity).dispose();
  }
}
