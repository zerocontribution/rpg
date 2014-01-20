package io.zerocontribution.winter.utils;

import com.artemis.Entity;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * @todo Hate this class.
 */
public class ClientGlobals {

    public static long time = System.currentTimeMillis();
    public static long timeDiff;

    public static Entity player;

    // TODO:
    public static List<Object> commands = new ArrayList<Object>();

}
