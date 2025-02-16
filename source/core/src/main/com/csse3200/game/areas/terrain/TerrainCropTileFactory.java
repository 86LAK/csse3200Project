package com.csse3200.game.areas.terrain;

import com.csse3200.game.components.ParticleEffectComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.configs.CropTileConfig;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;

public class TerrainCropTileFactory {

	private static final Logger logger = LoggerFactory.getLogger(TerrainCropTileFactory.class);

	private TerrainCropTileFactory() {
		throw new IllegalStateException("Instantiating static util class");
	}

	/**
	 * Creates a crop tile entity
	 *
	 * @param x x-position of the entity to be created
	 * @param y y-position of the entity to be created
	 * @return created crop tile entity
	 */
	public static Entity createTerrainEntity(float x, float y) {
		Vector2 position = new Vector2(x, y);
		return createTerrainEntity(position);
	}

	/**
	 * Creates a crop tile entity
	 *
	 * @param position position of the entity to be created
	 * @return created crop tile entity
	 */

	public static Entity createTerrainEntity(Vector2 position) {
		logger.debug("Creating crop tile at position {}", position);

		DynamicTextureRenderComponent renderComponent = new DynamicTextureRenderComponent("images/cropTile.png");
		renderComponent.setLayer(1);

		Entity tile = new Entity(EntityType.TILE)
				.addComponent(new ColliderComponent().setSensor(true))
				.addComponent(new PhysicsComponent())
				.addComponent(renderComponent)
				.addComponent(new ParticleEffectComponent())
				.addComponent(new CropTileComponent(CropTileConfig.INITIAL_WATER_CONTENT, CropTileConfig.INITIAL_SOIL_QUALITY));

		tile.setPosition(position);
		return tile;
	}
}
