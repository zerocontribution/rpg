package io.zerocontribution.winter.server;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.systems.server.ServerNetworkSystem;
import io.zerocontribution.winter.systems.server.ServerUpdateSystem;
import io.zerocontribution.winter.utils.ServerGlobals;

public class GameScreen implements Screen {

    WinterGameServer server;

    World world;

    public GameScreen(WinterGameServer server) {
        this.server = server;
        ServerGlobals.time = System.currentTimeMillis();

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

    @Override
    public void render(float v) {
        ServerGlobals.time = System.currentTimeMillis(); // TODO Remove
        world.process();
    }

    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
