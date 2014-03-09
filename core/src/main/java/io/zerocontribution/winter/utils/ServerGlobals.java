package io.zerocontribution.winter.utils;

import com.artemis.utils.Bag;
import io.zerocontribution.winter.server.maps.tiled.TiledMap;

public class ServerGlobals {
    public static long time = System.currentTimeMillis();
    public static Bag updates;
    public static TiledMap currentMap;
}
