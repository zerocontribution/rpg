package io.zerocontribution.winter.systems.client;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.network.Network;
import io.zerocontribution.winter.utils.ClientGlobals;

public class ClientNetworkSystem extends IntervalEntitySystem {

    private Client client;

    public ClientNetworkSystem(Client client, float interval) {
        super(Aspect.getEmpty(), interval);
        this.client = client;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        processSystem();
    }

    protected void processSystem() {
        if (ClientGlobals.commands.size() > 0) {
            client.sendTCP(new Network.ClientCommands(ClientGlobals.commands));
            ClientGlobals.commands.clear();
        }
    }

}
