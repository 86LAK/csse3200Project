package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.events.EventHandler;

import java.util.ArrayList;

/** the GameMap class is used to store and easily access and manage the components related to the game map */
public class GameMap {

    private final TerrainFactory terrainFactory;
    private final TiledMap tiledMap;
    private final EventHandler eventHandler;

    public GameMap(TerrainFactory terrainFactory) {
        this.terrainFactory = terrainFactory;
        this.tiledMap = new TiledMap();
        this.eventHandler = new EventHandler();
    }

    public TerrainFactory getTerrainFactory() {
        return terrainFactory;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Returns the size of the map
     * @return a copy of the GridPoint2 instance which contains the dimensions of the map
     */
    public GridPoint2 getMapSize() {
        return terrainFactory.getMapSize();
    }

    /**
     * Get the Cell in TiledMap, use for cell interaction
     * such as get and set tile, rotating Cell, and accessing the contents of the cell
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return the Cell
     */
    private TiledMapTileLayer.Cell getCell(int x, int y) {
        return ((TiledMapTileLayer) this.tiledMap.getLayers().get(0)).getCell(x, y);
    }

    /**
     * Gets the tile at a specified world coordinate position
     * @param x x coordinate
     * @param y y coordinate
     * @return a TerrainTile
     */
    public TerrainTile getTile(int x, int y) {
        GridPoint2 max = this.getMapSize();

        if (x > max.x || y > max.y) {
            throw new IndexOutOfBoundsException("Bad Input: Coordinate position out of bounds");
        }

        return (TerrainTile) getCell(x, y).getTile();
    }

    /**
     * Conversion function: gets the centre of a tile from a coordinate position
     * e.g. on a 4x4 tile grid, (0, 0) in the world translates to (8, 56) on the pixel grid
     * @param x x coordinate
     * @param y y coordinate
     * @return a Vector2 value
     */
    public Vector2 worldCoordinatesToPixelPosition(int x, int y) {
        GridPoint2 max = this.getMapSize();

        if (x > max.x || y > max.y) {
            throw new IndexOutOfBoundsException("Bad Input: Coordinate position out of bounds");
        }

        Vector2 pixelPositions = new Vector2();
        int xPixel = (16 * x) + 8;
        int y2 = (16 * y) + 8;
        // post process y2 to convert (0,0) location to bottom left
        int yPixel = Gdx.graphics.getHeight() - 1 - y2;
        pixelPositions.x = xPixel;
        pixelPositions.y = yPixel;
        return pixelPositions;
    }

    /**
     * Conversion function: gets the centre of a tile from a coordinate position
     * e.g. on a 4x4 tile grid, (6, 50) on the pixel grid translates to (0, 0) in the world
     * @param xPixel x pixel screen coordinate
     * @param yPixel y pixel screen coordinate
     * @return a Vector2 value
     */
    public Vector2 pixelPositionToWorldCoordinates(int xPixel, int yPixel) {
        Vector2 worldCoordinates = new Vector2();
        int x = Math.floorDiv(xPixel, 16);
        // preprocess y2 to convert (0,0) to top left
        int y2 = Gdx.graphics.getHeight() - 1 - yPixel;
        int y = Math.floorDiv(y2, 16);
        worldCoordinates.x = x;
        worldCoordinates.y = y;
        return worldCoordinates;
    }

    /**
     * Returns the terrain category at a specified tile
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return the TerrainCategory of the specified tile
     */
    public TerrainTile.TerrainCategory getTileTerrainCategory(int x, int y) {
        return this.getTile(x, y).getTerrainCategory();
    }

    /**
     * Sets the terrainCategory at the specified tile to the provided terrainCategory
     * @param terrainCategory terrinCategory to set the TerrainTile to
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     */
    public void setTileTerrainCategory(TerrainTile.TerrainCategory terrainCategory, int x, int y) {
        this.getTile(x, y).setTerrainCategory(terrainCategory);
    }

    /**
     * Returns whether the specified terrain is traversable
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return true if the tile is traversable and false if not
     */
    public boolean isTileTraversable(int x, int y) {
        return this.getTile(x, y).isTraversable();
    }

    /**
     * Returns whether the specified terrain is occupied
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return true if the tile is occupied and false if not
     */
    public boolean isTileOccupied(int x, int y) {
        return this.getTile(x, y).isOccupied();
    }

    /**
     * Sets the specified tile as being occupied
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     */
    public void setTileOccupied(int x, int y) {
        this.getTile(x, y).setOccupied();
    }

    /**
     * Sets the specified tile as being unoccupied
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     */
    public void setTileUnoccupied(int x, int y) {
        this.getTile(x, y).setUnOccupied();
    }

    /**
     * Returns whether the specified terrain is tillable
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return true if the tile is tillable and false if not
     */
    public boolean isTileTillable(int x, int y) {
        return this.getTile(x, y).isTillable();
    }
}


