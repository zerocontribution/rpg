package io.zerocontribution.winter.client;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.network.Message;
import io.zerocontribution.winter.network.Network;
import io.zerocontribution.winter.utils.ClientGlobals;

import java.io.IOException;
import java.util.HashMap;

public class GameClient {

    public String localName;
    public Client client;

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
        Entity entity = WinterGame.world.createEntity();
        WinterGame.world.addEntity(entity);

        serverToClient.put(serverID, entity);
        clientToServer.put(entity.getId(), serverID);

        Log.info("Client", "Created new entity " + serverID + " -> " + entity.getId());

        return entity;
    }

    private <T extends Component> T createComponent(Entity entity, Class<T> type) {
        Log.info("Client", entity.toString() + " <- " + type.getSimpleName());
        T component = null;

        try {
            component = type.newInstance();
            entity.addComponent(component);
            WinterGame.world.changedEntity(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return component;
    }

    public void connect(String serverIP) {
        client = new Client(1024 * 10, 1024 * 10);
        Network.register(client);

        Thread kryoThread = new Thread(client);
        kryoThread.setDaemon(true);
        kryoThread.start();

        try {
            client.connect(5000, serverIP, Network.serverPort, Network.serverPort + 1);
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
        Log.info("Client", "RTT: " + client.getReturnTripTime());
        ClientGlobals.timeDiff = client.getReturnTripTime();
    }

    public void sendLogin() {
        Network.Login msg = new Network.Login(localName, Network.version);
        client.sendTCP(msg);
    }

    public void sendLogout() {
        Network.Logout msg = new Network.Logout();
        client.sendTCP(msg);
    }

    public void sendStartGame() {
        Network.StartGame msg = new Network.StartGame("isometric");
        client.sendTCP(msg);
    }

    private class ClientNetworkListener extends Listener {

        @Override
        public void connected(Connection connection) {
            Log.info("Client", "Connected to server: " + connection.toString());
        }

        @Override
        public void disconnected(Connection connection) {
            Log.info("Client", "Disconnected from server: " + connection.toString());
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
            if (o instanceof Message) {
                ((Message) o).receive(pc);
            } else {
                if (o instanceof FrameworkMessage) {
                    return;
                }
                Log.warn("Client", "Unknown message type: " + o.getClass().getSimpleName());
            }
        }

    }

}
