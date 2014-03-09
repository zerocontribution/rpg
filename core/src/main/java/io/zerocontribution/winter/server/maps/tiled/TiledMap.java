package io.zerocontribution.winter.server.maps.tiled;

import com.badlogic.gdx.maps.Map;

public class TiledMap extends Map {

    private TiledMapTileSets tileSets;

    public TiledMap() {
        tileSets = new TiledMapTileSets();
    }

    public TiledMapTileSets getTileSets() {
        return tileSets;
    }
}
