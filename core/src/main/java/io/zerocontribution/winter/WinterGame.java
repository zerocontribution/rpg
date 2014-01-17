package io.zerocontribution.winter;

import com.artemis.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.zerocontribution.winter.screens.IsoTiledMapScreen;
import io.zerocontribution.winter.screens.MenuScreen;
import io.zerocontribution.winter.server.GameServer;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;
import io.zerocontribution.winter.systems.server.ServerNetworkSystem;

/**
 * @todo Move ArtemisState stuff into WinterGame.
 * @todo ClientGlobals stuff should also live in here; I think.
 * @todo The server instance, if the player is a host, would also be attached as a static property.
 */
public class WinterGame extends Game {

    @Override
    public void create () {
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

    public void startServer() {
        new Thread(new GameServer()).start();
    }

}
