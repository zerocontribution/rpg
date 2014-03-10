package io.zerocontribution.winter.systems.server;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.components.BaseComponent;
import io.zerocontribution.winter.components.Update;
import io.zerocontribution.winter.components.Velocity;
import io.zerocontribution.winter.network.*;
import io.zerocontribution.winter.server.GameServer;
import io.zerocontribution.winter.server.maps.tiled.TmxMapLoader;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.ServerGlobals;

import java.io.IOException;
import java.util.ArrayList;

public class ServerNetworkSystem extends VoidEntitySystem {

    private Server server;

    @Override
    protected void initialize() {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        Network.register(server);

        new Thread(server).start();

        server.addListener(new ServerNetworkListener());

        try {
            server.bind(Network.serverPort, Network.serverPort + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processSystem() {
        Log.debug("Server", "Sending updates");
        server.sendToAllTCP(ServerGlobals.updates.toArray());
        ServerGlobals.updates = null;
    }

    @Override
    protected boolean checkProcessing() {
        return ServerGlobals.updates != null;
    }

    public void handleLoginMessage(PlayerConnection connection, Network.Login packet) {
        if (packet.version != Network.version) {
            Log.error("Wrong version");
            connection.close();
            return;
        }

        if (packet.name == null || packet.name.equals(Constants.UI.DEFAULT_PLAYER_NAME) || packet.name.trim().length() == 0) {
            packet.name = "CannonFodder" + connection.getID();
        }

        Log.info("Server", packet.name + " has logged in");

        // TODO World system (to be SpawnerSystem?) should dictate where to spawn
        int pos = 5 + connection.getID();

        connection.player = EntityFactory.createPlayer(world, packet.name, pos, pos);
        connection.player.addToWorld();

        server.sendToUDP(connection.getID(), new LoginResponse(connection.player.getId()));

        ImmutableBag<Entity> entities = world.getManager(GroupManager.class).getEntities(Constants.Groups.CLIENT);
        Log.info("Server", "Sending initialization data for " + entities.size() + " entities");
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            sendComponents(connection, entity);
        }

        Log.info("Server", "Sending new player data to other players");
        for (Connection conn : server.getConnections()) {
            if (conn.getID() != connection.getID()) {
                sendComponents((PlayerConnection) conn, connection.player);
            }
        }
    }

    public void handleLogoutMessage(PlayerConnection connection, Network.Logout packet) {
        world.deleteEntity(connection.player);

        // TODO: Broadcast to other players.

        connection.close();
    }

    public void handleStartGame(PlayerConnection connection, Network.StartGame packet) {
        if (connection.getID() != 1) {
            Log.error("Server", "StartGame message received from non-host: " + connection.getID());
            return;
        }

        ServerGlobals.loadServerMap(packet.map);

        server.sendToAllTCP(new StartGameResponse(packet.map));
    }

    // TODO Move this all out into command handlers
    public void handleClientCommands(PlayerConnection connection, Network.ClientCommands clientCommands) {
        for (int i = 0; i < clientCommands.commands.size(); i++) {
            Command c = clientCommands.commands.get(i);
            if (c instanceof ActionCommand) {
                ActionCommand command = (ActionCommand) c;
                Velocity velocity = world.getMapper(Velocity.class).get(connection.player);

                if (command.action == ActionCommand.Action.MOVE_LEFT) {
                    velocity.setX(command.lifecycle == ActionCommand.Lifecycle.START ? -Constants.PLAYER_SPEED : 0);
                } else if (command.action == ActionCommand.Action.MOVE_RIGHT) {
                    velocity.setX(command.lifecycle == ActionCommand.Lifecycle.START ? Constants.PLAYER_SPEED : 0);
                }

                if (command.action == ActionCommand.Action.MOVE_UP) {
                    velocity.setY(command.lifecycle == ActionCommand.Lifecycle.START ? Constants.PLAYER_SPEED : 0);
                } else if (command.action == ActionCommand.Action.MOVE_DOWN) {
                    velocity.setY(command.lifecycle == ActionCommand.Lifecycle.START ? -Constants.PLAYER_SPEED : 0);
                }

                connection.player.addComponent(new Update());
                connection.player.changedInWorld();
            } else if (c instanceof AbilityCommand) {
                Log.info("Server", "AbilityCommands not supported yet");
            } else {
                Log.error("Server", "Unknown command type: " + c.getClass().getSimpleName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void sendComponents(PlayerConnection connection, Entity entity) {
        Log.debug("Server", "Sending components for " + entity.toString());

        Bag<Component> components = entity.getComponents(new Bag<Component>());
        ArrayList list = new ArrayList();

        for (int i = 0; i < components.size(); i++) {
            BaseComponent c = (BaseComponent) components.get(i);
            Log.debug("Server", "  Component " + c.toString());

            Object trans = c.create(entity);
            if (trans != null) {
                Log.debug("Server", "    Added");
                list.add(trans);
            }
        }

        if (!list.isEmpty()) {
            connection.sendTCP(list.toArray());
        }
    }

    private class ServerNetworkListener extends Listener {
        @Override
        public void connected(Connection connection) {}

        @Override
        public void disconnected(Connection connection) {}

        @Override
        public void received(Connection connection, Object o) {
            PlayerConnection pc = (PlayerConnection) connection;

            if (o instanceof Network.Login) {
                handleLoginMessage(pc, (Network.Login) o);
            } else if (o instanceof Network.Logout) {
                handleLogoutMessage(pc, (Network.Logout) o);
            } else if (o instanceof Network.StartGame) {
                handleStartGame(pc, (Network.StartGame) o);
            } else if (o instanceof Network.ClientCommands) {
                handleClientCommands(pc, (Network.ClientCommands) o);
            } else {
                if (o instanceof FrameworkMessage) {
                    return;
                }
                Log.warn("Server", "Unhandled message: " + o.getClass().getSimpleName());
            }
        }
    }

}
