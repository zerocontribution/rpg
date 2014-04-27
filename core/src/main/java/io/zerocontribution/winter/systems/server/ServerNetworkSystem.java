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
import io.zerocontribution.winter.DeprecatedAssets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.assets.MapAsset;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.network.*;
import io.zerocontribution.winter.utils.ServerGlobals;

import java.io.IOException;
import java.util.ArrayList;

public class ServerNetworkSystem extends VoidEntitySystem {

    private Server server;

    @Override
    protected void initialize() {
        // TODO These are pretty big-big buffers... :\
        server = new Server(1024 * 10, 1024 * 10) {
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
            packet.name = Constants.DEFAULT_PLAYER_NAME + connection.getID();
        }

        Log.info("Server", packet.name + " has logged in");

        connection.player = ServerGlobals.entityFactory.createPlayer(world, packet.name);
        connection.player.addToWorld();

        server.sendToTCP(connection.getID(), new LoginResponse(connection.player.getId()));

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

    public void handleGameLoaded(PlayerConnection connection, Network.GameLoaded packet) {
        // TODO World system (to be SpawnerSystem?) should dictate where to spawn
        int pos = 5 + connection.getID();

        Log.info("Server", "Creating world entity for " + connection.player);

        ServerGlobals.entityFactory.createWorldPlayer(connection.player, pos, pos);
        connection.player.changedInWorld();

        for (Connection conn : server.getConnections()) {
            sendComponents((PlayerConnection) conn, connection.player);
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

        MapAsset map = DeprecatedAssets.maps.get(packet.map);
        if (map == null) {
            // TODO sendToAllTCP(new ServerErrorResponse("No map ..."))
            throw new RuntimeException("No map asset found for " + packet.map);
        }

        ServerGlobals.loadServerMap(map);
        ServerGlobals.entityFactory.createMap(world).addToWorld();

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
                AbilityCommand command = (AbilityCommand) c;

                // TODO: Do not add the Update component here: We don't publish actions?
                ActionInput input = new ActionInput(command.abilityId, world.getEntity(command.targetEntityId));
                connection.player.addComponent(input);
                connection.player.changedInWorld();
            } else {
                Log.error("Server", "Unknown command type: " + c.getClass().getSimpleName());
            }
        }
    }

    public void handlePlayerLobbyState(PlayerConnection connection, PlayerLobbyState playerLobbyState) {
        connection.player.getComponent(Player.class).setGameClass(playerLobbyState.selectedClass);
        connection.player.addComponent(new Update());
        connection.player.changedInWorld();
    }

    public void handleChatMessage(PlayerConnection connection, ChatMessage message) {
        message.name = connection.player.getComponent(Player.class).name;
        Log.info("Server", "Message broadcast (ALL) from " + message.name + ": '" + message.message + "'");
        server.sendToAllTCP(message);
    }

    @SuppressWarnings("unchecked")
    private void sendComponents(PlayerConnection connection, Entity entity) {
        Log.debug("Server", "Sending components for " + entity.toString());

        Bag<Component> components = entity.getComponents(new Bag<Component>());
        ArrayList list = new ArrayList();

        for (int i = 0; i < components.size(); i++) {
            BaseComponent c = (BaseComponent) components.get(i);

            Object trans = c.create(entity);
            if (trans != null) {
                Log.debug("Server", "  Component " + c.toString());
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

            if (o instanceof Network.ClientCommands) {
                handleClientCommands(pc, (Network.ClientCommands) o);
            } else if (o instanceof Network.Login) {
                handleLoginMessage(pc, (Network.Login) o);
            } else if (o instanceof Network.Logout) {
                handleLogoutMessage(pc, (Network.Logout) o);
            } else if (o instanceof Network.StartGame) {
                handleStartGame(pc, (Network.StartGame) o);
            } else if (o instanceof Network.GameLoaded) {
                handleGameLoaded(pc, (Network.GameLoaded) o);
            } else if (o instanceof PlayerLobbyState) {
                handlePlayerLobbyState(pc, (PlayerLobbyState) o);
            } else if (o instanceof ChatMessage) {
                handleChatMessage(pc, (ChatMessage) o);
            } else {
                if (o instanceof FrameworkMessage) {
                    return;
                }
                Log.warn("Server", "Unhandled message: " + o.getClass().getSimpleName());
            }
        }
    }

}
