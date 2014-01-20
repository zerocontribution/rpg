package io.zerocontribution.winter.server;

import com.badlogic.gdx.Game;

public class WinterGameServer extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }

}
