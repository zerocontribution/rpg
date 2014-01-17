package io.zerocontribution.winter.client;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.ApplicationListener;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;
import io.zerocontribution.winter.utils.ClientGlobals;

import java.util.HashMap;

/**
 * @todo Move the world stuff into a client screen.
 * @todo Move the client/server id mapping into the ClientNetworkSystem
 * @todo ... Then delete this class.
 */
public class ArtemisState {

    HashMap<Integer, Entity> serverToClient = new HashMap<Integer, Entity>();
    HashMap<Integer, Integer> clientToServer = new HashMap<Integer, Integer>();

    public <T extends Component> T getComponent(Entity entity, Class<T> type) {
        T component = entity.getComponent(type);
        if(component == null) {
            component = createComponent(entity, type);
        }
        return type.cast(component);
    }

    public int toServerID(int clientID) {
        return clientToServer.get(clientID);
    }

    public Entity toClientEntity(int serverID) {
        Entity entity = serverToClient.get(serverID);
        if(entity == null) {
            entity = addEntity(serverID);
        }
        return entity;
    }

    private Entity addEntity(int serverID) {
        Entity entity = ClientGlobals.world.createEntity();
        ClientGlobals.world.addEntity(entity);

        serverToClient.put(serverID, entity);
        clientToServer.put(entity.getId(), serverID);

        Log.info("Created new entity " + serverID + " -> " + entity.getId());

        return entity;
    }

    private <T extends Component> T createComponent(Entity entity, Class<T> type) {
        Log.info(entity.toString() + " <- " + type.getSimpleName());
        T component = null;

        try {
            component = type.newInstance();
            entity.addComponent(component);
            ClientGlobals.world.changedEntity(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return component;
    }

}
