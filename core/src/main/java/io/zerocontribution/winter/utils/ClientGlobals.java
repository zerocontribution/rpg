package io.zerocontribution.winter.utils;

import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.zerocontribution.winter.network.Command;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * @todo Hate this class.
 */
public class ClientGlobals {

    public static long timeDiff;
    public static int ping = 0;

    public static Entity player;

    public static Bag<Command> commands = new Bag<Command>();

    public static TiledMap currentMap;

    public static void loadClientMap(String mapName) {
        currentMap = new TmxMapLoader().load("maps/" + mapName + ".tmx");
    }
}
