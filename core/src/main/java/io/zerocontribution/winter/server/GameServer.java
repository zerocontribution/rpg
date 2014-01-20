package io.zerocontribution.winter.server;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.systems.server.ServerNetworkSystem;
import io.zerocontribution.winter.systems.server.ServerUpdateSystem;
import io.zerocontribution.winter.utils.ServerGlobals;

/**
 * @todo Need a way to postpone world creation until the host has launched the game. The run loop should be pretty dumb
 *       for awhile until then; using a conditional to actually run game code vs. lobby code. Maybe lobby code is its
 *       own world that gets canned when the game is launched?
 */
public class GameServer implements Runnable {

    public void run() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Winter Game";
        config.useGL20 = true;
        config.width = 1200;
        config.height = 720;
        new LwjglApplication(new WinterGameServer(), config);
    }

}
