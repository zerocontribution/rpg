package io.zerocontribution.winter.utils;

import com.artemis.Entity;
import com.artemis.World;
import io.zerocontribution.winter.client.ArtemisState;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;

import java.util.ArrayList;
import java.util.List;

public class ClientGlobals {

    public static long time = System.currentTimeMillis();
    public static long timeDiff;

    public static World world;

    public static Entity player;

    public static ArtemisState artemis = new ArtemisState();
    public static ClientNetworkSystem network;

    // TODO:
    public static List<Object> commands = new ArrayList<Object>();

}
