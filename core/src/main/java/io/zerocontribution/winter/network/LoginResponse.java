package io.zerocontribution.winter.network;

import com.artemis.Entity;
import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.utils.ClientGlobals;

public class LoginResponse extends EventMessage {
    private int id = 0;

    public LoginResponse() {}
    public LoginResponse(int id) {
        this.id = id;
    }

    @Override
    public void receive(Connection pc) {
        Entity entity = ClientGlobals.artemis.toClientEntity(id);
        ClientGlobals.player = entity;
        entity.addToWorld();
    }

    public String toString() {
        return "LoginResponse(" + id + ")";
    }
}
