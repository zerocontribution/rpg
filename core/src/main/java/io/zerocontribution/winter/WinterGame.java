package io.zerocontribution.winter;

import com.artemis.World;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.zerocontribution.winter.client.GameClient;
import io.zerocontribution.winter.screens.MenuScreen;
import io.zerocontribution.winter.server.GameServer;

/**
 * @todo ClientGlobals stuff should also live in here; I think.
 */
public class WinterGame extends Game {

    private static WinterGame instance;

    public static World world;

    public static GameClient gameClient;

    private Thread gameServer;

    public static WinterGame getInstance() {
        return instance;
    }

    @Override
    public void create () {
        instance = this;
        gameClient = new GameClient();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render () {
        super.render();
        // TODO Add world reset
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            try {
                setScreen(getScreen().getClass().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isHost() {
        return gameServer != null;
    }

    public void startServer() {
        gameServer = new Thread(new GameServer());
        gameServer.start();
    }

}
