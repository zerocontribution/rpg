package io.zerocontribution.winter.server;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.systems.MovementSystem;
import io.zerocontribution.winter.systems.server.AIProcessingSystem;
import io.zerocontribution.winter.systems.server.ServerCollisionSystem;
import io.zerocontribution.winter.systems.server.ServerNetworkSystem;
import io.zerocontribution.winter.systems.server.ServerUpdateSystem;
import io.zerocontribution.winter.utils.ServerGlobals;

public class GameScreen implements Screen {

    GameServer server;
    World world;

    public GameScreen(GameServer server) {
        this.server = server;

        if (Constants.DEBUG) {
            Log.DEBUG();
        }

        world = new World();

        world.setManager(new GroupManager());
        world.setManager(new TagManager());

        world.setSystem(new ServerNetworkSystem());
        world.setSystem(new AIProcessingSystem());
        world.setSystem(new ServerCollisionSystem());
        world.setSystem(new MovementSystem(ServerGlobals.currentMap));
        world.setSystem(new ServerUpdateSystem(1 / 20.0f));

        world.initialize();
    }

    @Override
    public void render(float v) {
        world.setDelta(v);
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
