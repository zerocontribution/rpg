package io.zerocontribution.winter.server.maps.tiled;

import com.badlogic.gdx.maps.MapProperties;

public class StaticTiledMapTile implements TiledMapTile {

    private int id;
    private MapProperties properties;

    public StaticTiledMapTile() {}

    public StaticTiledMapTile(StaticTiledMapTile copy) {
        this.id = copy.id;
        if (copy.properties != null) {
            getProperties().putAll(copy.properties);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public MapProperties getProperties() {
        if (properties == null) {
            properties = new MapProperties();
        }
        return properties;
    }

}
