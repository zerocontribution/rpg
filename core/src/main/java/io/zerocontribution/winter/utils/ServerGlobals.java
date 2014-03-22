package io.zerocontribution.winter.utils;

import io.zerocontribution.winter.network.Network;
import io.zerocontribution.winter.server.ServerEntityFactory;
import io.zerocontribution.winter.server.maps.tiled.TiledMap;
import io.zerocontribution.winter.server.maps.tiled.TmxMapLoader;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServerGlobals {
    public static ArrayList<Network.EntityData> updates;
    public static TiledMap currentMap;
    public static ServerEntityFactory entityFactory = new ServerEntityFactory();

    public static AtomicInteger entitiesActive = new AtomicInteger();
    public static AtomicLong entitiesCreated = new AtomicLong();
    public static AtomicLong entitiesDeleted = new AtomicLong();

    public static void loadServerMap(String mapName) {
        currentMap = new TmxMapLoader().load("assets/maps/" + mapName + ".tmx");
    }
}
