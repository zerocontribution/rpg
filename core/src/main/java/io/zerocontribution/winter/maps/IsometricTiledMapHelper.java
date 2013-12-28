package io.zerocontribution.winter.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * Helper class to deal with all of the fucking oddities that libGDX seems to have.
 *
 * Unlike other tiled renderers; libGDX uses an inversed y-indexed coordinate system. That makes a lot of the
 * necessary calculations custom.
 */
public class IsometricTiledMapHelper {

    private TiledMap map;

    public IsometricTiledMapHelper(TiledMap map) {
        this.map = map;
    }

    public MapObject getSpawnPointObject() {
        return map.getLayers().get("Objects").getObjects().get("spawn");
    }

    public Vector2 getSpawnPoint() {
        MapObject object = getSpawnPointObject();

        if (object != null) {
            int x = object.getProperties().get("x", Integer.class);
            int y = object.getProperties().get("y", Integer.class) - getPixelHeight(); // FFS libGDX.
            return convertToScreen(x, y);
        }

        // TODO Add logging if no spawn point
        return new Vector2(0, 0);
    }

    public Vector2 convertToWorld(float screenX, float screenY) {
        Vector2 world = new Vector2();

        float tileWidth = map.getProperties().get("tilewidth", Integer.class);
        float tileHeight = map.getProperties().get("tileheight", Integer.class);

        world.x = (screenY / tileHeight) + (screenX / tileWidth);
        world.y = (screenX / tileWidth) - (screenY / tileHeight);

        return world;
    }

    public Vector2 convertToWorld(Vector2 screen) {
        return convertToWorld(screen.x, screen.y);
    }

    public Vector2 convertToScreen(int worldX, int worldY) {
        Vector2 screen = new Vector2();

        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        screen.x = (worldX + worldY) * (tileWidth / 2); // Supposed to be worldX - worldY; but results seem better this way?
        screen.y = (worldX + worldY) * (tileHeight / 2);

        return screen;
    }

    public int getPixelWidth() {
        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);

        return mapWidth * tilePixelWidth;
    }

    public int getPixelHeight() {
        MapProperties prop = map.getProperties();

        int mapHeight = prop.get("height", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        return mapHeight * tilePixelHeight;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return (TiledMapTileLayer) map.getLayers().get("Ground");
    }

}
