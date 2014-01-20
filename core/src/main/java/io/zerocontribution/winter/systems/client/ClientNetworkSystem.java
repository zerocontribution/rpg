package io.zerocontribution.winter.systems.client;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.utils.ClientGlobals;

import java.io.IOException;

/**
 * @todo IntervalEntitySystem?
 */
public class ClientNetworkSystem extends EntitySystem {

    private Client client;
    private long interval;
    private long acc;

    public ClientNetworkSystem(Client client, long interval) {
        super(Aspect.getEmpty());
        this.interval = interval;
        this.client = client;
    }

    @Override
    protected boolean checkProcessing() {
        if (ClientGlobals.time >= acc) {
            acc = ClientGlobals.time + interval;
            return true;
        }
        return false;
    }


    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        processSystem();
    }

    protected void processSystem() {
        if (ClientGlobals.commands.size() > 0) {
            Log.info("Sending client commands");
            client.sendUDP(ClientGlobals.commands);
            ClientGlobals.commands.clear();
        }
    }

}
