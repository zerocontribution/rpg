package io.zerocontribution.winter.network;

import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.screens.GameLoadScreen;

public class StartGameResponse extends EventMessage {
    public String map;

    public StartGameResponse() {}
    public StartGameResponse(String map) {
        this.map = map;
    }

    @Override
    public void receive(Connection pc) {
        WinterGame game = WinterGame.getInstance();
        game.setScreen(new GameLoadScreen(game, map));
    }
}
