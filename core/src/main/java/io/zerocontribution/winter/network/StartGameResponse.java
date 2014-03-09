package io.zerocontribution.winter.network;

import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class StartGameResponse extends EventMessage {
    public String map;

    public StartGameResponse() {}
    public StartGameResponse(String map) {
        this.map = map;
    }

    @Override
    public void receive(Connection pc) {
        WinterGame game = WinterGame.getInstance();

        // We're currently in the Client thread.
        List<Object> args =  new ArrayList<Object>(1);
        args.add(map);
        game.changeScreen(GameScreen.class, args);
    }
}
