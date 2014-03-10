package io.zerocontribution.winter.utils;

import com.artemis.utils.Bag;
import io.zerocontribution.winter.network.Network;
import io.zerocontribution.winter.server.maps.tiled.TiledMap;
import io.zerocontribution.winter.server.maps.tiled.TmxMapLoader;

import java.util.ArrayList;

public class ServerGlobals {
    public static ArrayList<Network.EntityData> updates;
    public static TiledMap currentMap;

    public static void loadServerMap(String mapName) {
        currentMap = new TmxMapLoader().load("assets/maps/" + mapName + ".tmx");
    }
}
