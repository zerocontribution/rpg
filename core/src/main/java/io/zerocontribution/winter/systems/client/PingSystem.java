package io.zerocontribution.winter.systems.client;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.kryonet.Client;
import io.zerocontribution.winter.utils.ClientGlobals;

public class PingSystem extends IntervalEntitySystem {

    Client client;

    public PingSystem(Client client) {
        super(Aspect.getEmpty(), 5.0f);
        this.client = client;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        ClientGlobals.ping = client.getReturnTripTime();
        client.updateReturnTripTime();
    }
}
