package io.zerocontribution.winter.systems.client;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.components.BaseComponent;
import io.zerocontribution.winter.components.EntityComponent;
import io.zerocontribution.winter.network.*;
import io.zerocontribution.winter.utils.ClientGlobals;

import java.io.IOException;

/**
 * @todo IntervalEntitySystem?
 */
public class ClientNetworkSystem extends EntitySystem {

    private Client client;
    private long interval;
    private long acc;

    public ClientNetworkSystem(long interval) {
        super(Aspect.getEmpty());
        this.interval = interval;
    }

    @Override
    protected boolean checkProcessing() {
        if (ClientGlobals.time >= acc) {
            acc = ClientGlobals.time + interval;
            return true;
        }
        return false;
    }

    /**
     * @todo Change to ticks
     */
    @Override
    protected void initialize() {
        client = new Client();
        Network.register(client);

        Thread kryoThread = new Thread(client);
        kryoThread.setDaemon(true);
        kryoThread.start();

        try {
            client.connect(5000, Network.serverHost, Network.serverPort, Network.serverPort + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.addListener(new ClientNetworkListener());

        // TODO: This should get refreshed every 5 seconds or so.
        client.updateReturnTripTime();
        while (client.getReturnTripTime() == -1) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
        Log.info("RTT: " + client.getReturnTripTime());
        ClientGlobals.timeDiff -= client.getReturnTripTime();
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

    public void sendLogin(String localPlayerName) {
        Network.Login msg = new Network.Login(localPlayerName, Network.version);
        client.sendTCP(msg);
    }

    public void sendLogout() {
        Network.Logout msg = new Network.Logout();
        client.sendTCP(msg);
    }

    private class ClientNetworkListener extends Listener {

        @Override
        public void connected(Connection connection) {
            Log.info("Connected to server: " + connection.toString());
        }

        @Override
        public void disconnected(Connection connection) {
            Log.info("Disconnected from server: " + connection.toString());
        }

        @Override
        public void received(Connection pc, Object o) {
            if (o instanceof Bag) {
                Bag bag = (Bag) o;
                for (int i = 0; i < bag.size(); i++) {
                    handlePacket(pc, bag.get(i));
                }
            }
            if (o instanceof Object[]) {
                for (Object packet : (Object[]) o) {
                    handlePacket(pc, packet);
                }
            } else {
                handlePacket(pc, o);
            }
        }

        public void handlePacket(final Connection pc, final Object o) {
            Log.debug("Received: " + o.toString());

            if (o instanceof Message) {
                ((Message) o).receive(pc);
            } else {
                Log.warn("Unknown message type: " + o.getClass().getSimpleName());
            }
        }

    }

}
