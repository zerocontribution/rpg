package io.zerocontribution.winter;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {

    public static TiledMap currentMap;

    public static TiledMap loadMap(String mapPath) {
        currentMap = new TmxMapLoader().load(mapPath);
        return currentMap;
    }

}
