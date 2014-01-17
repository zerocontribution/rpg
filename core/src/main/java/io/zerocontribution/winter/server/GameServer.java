package io.zerocontribution.winter.server;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.systems.server.ServerNetworkSystem;
import io.zerocontribution.winter.systems.server.ServerUpdateSystem;
import io.zerocontribution.winter.utils.ServerGlobals;

public class GameServer implements Runnable {

    World world;

    public GameServer() {
        if (Constants.DEBUG) {
            Log.DEBUG();
        }

        world = new World();

        world.setManager(new GroupManager());
        world.setManager(new TagManager());

        world.setSystem(new ServerUpdateSystem(20));
        world.setSystem(new ServerNetworkSystem());

        // Other systems...

        world.initialize();

        // TODO Initialize map
        // TODO Add entities to world
        // TODO Send message to clients that the world is ready
    }

    public void run() {
        while (true) {
            long ctime = System.currentTimeMillis();
            world.setDelta((ctime - ServerGlobals.time) / 1000.0f);
            ServerGlobals.time = ctime;

            world.process();

            long tick = System.currentTimeMillis() / ctime;
            if (tick < 20) {
                try {
                    Thread.sleep(20 - tick);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
