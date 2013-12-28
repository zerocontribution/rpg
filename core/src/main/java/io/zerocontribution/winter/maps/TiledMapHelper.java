package io.zerocontribution.winter.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class TiledMapHelper extends com.badlogic.gdx.maps.tiled.TiledMap {

    private TiledMap map;

    public TiledMapHelper(TiledMap map) {
        this.map = map;
    }

    public MapObject getSpawnPoint() {
        return map.getLayers().get("Objects").getObjects().get("spawn");
    }

    // TODO The math here is totally not correct... but it's close enough for now.
    public Vector2 toScreenCoords(MapObject object) {
        float halfWidth = getPixelWidth() * 0.5f, halfHeight = getPixelHeight() * 0.5f;

        MapProperties prop = object.getProperties();

        float x = halfWidth + (float) map.getProperties().get("tilewidth", Integer.class) - (float) prop.get("x", Integer.class);
        float y = (float) (halfHeight + map.getProperties().get("tileheight", Integer.class) - (prop.get("y", Integer.class) * Math.cos(30)));

        return new Vector2(x, y);
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

}
