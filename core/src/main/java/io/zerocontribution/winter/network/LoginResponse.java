package io.zerocontribution.winter.network;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.components.LocalPlayer;
import io.zerocontribution.winter.utils.ClientGlobals;

public class LoginResponse extends EventMessage {
    private int id = 0;

    public LoginResponse() {}
    public LoginResponse(int id) {
        this.id = id;
    }

    @Override
    public void receive(Connection pc) {
        Entity e = WinterGame.gameClient.toClientEntity(id);
        ClientGlobals.player = e;

        e.addComponent(new LocalPlayer());

        WinterGame.world.getManager(TagManager.class).register(Constants.Tags.LOCAL_PLAYER, e);

        e.changedInWorld();
    }

    public String toString() {
        return "LoginResponse(" + id + ")";
    }
}
