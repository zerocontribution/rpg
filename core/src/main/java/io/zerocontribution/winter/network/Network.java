package io.zerocontribution.winter.network;

import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import io.zerocontribution.winter.struct.Delay;
import io.zerocontribution.winter.struct.Directions;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.components.*;

public class Network {

    public static int version = 1;
    public static int serverPort = 58016;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        for (Class<?> clazz : Network.class.getDeclaredClasses()) {
            kryo.register(clazz);
        }

        kryo.register(Object[].class);
        kryo.register(ObjectMap.class);
        kryo.register(Bag.class);
        kryo.register(Rectangle.class);
        kryo.register(Directions.class);
        kryo.register(State.class);
        kryo.register(BaseComponent.class);
        kryo.register(EntityComponent.class);
        kryo.register(Actor.class);
        kryo.register(AnimationName.class); // Derive from an Actor property?
        kryo.register(AnimationTimer.class); // TODO Remove?
        kryo.register(Blocking.class);
        kryo.register(Bounds.class);
        kryo.register(CombatAction.class);
        kryo.register(Condition.class);
        kryo.register(Damage.class);
        kryo.register(Delay.class);
        kryo.register(Dimensions.class);
        kryo.register(Update.class);
        kryo.register(Facing.class);
        kryo.register(GridPosition.class);
        kryo.register(Name.class);
        kryo.register(Npc.class);
        kryo.register(Player.class);
        kryo.register(Position.class);
        kryo.register(SpriteColor.class);
        kryo.register(Stats.class);
        kryo.register(TargetGridPosition.class); // TODO Don't want to transfer this.
        kryo.register(Velocity.class);
        kryo.register(LoginResponse.class);
        kryo.register(StartGameResponse.class);
        kryo.register(Command.class);
        kryo.register(AbilityCommand.class);
        kryo.register(ActionCommand.Action.class);
        kryo.register(ActionCommand.Lifecycle.class);
        kryo.register(ActionCommand.class);
    }

    public static class Login extends EventMessage {
        public String name;
        public int version;

        public Login() {}
        public Login(String name, int version) {
            this.name = name;
            this.version = version;
        }
    }

    public static class Logout extends EventMessage {
        public String name;

        public Logout() {}
        public Logout(String name) {
            this.name = name;
        }
    }

    public static class StartGame extends EventMessage {
        public String map;

        public StartGame() {}
        public StartGame(String map) {
            this.map = map;
        }
    }

//    public static class ClientCommands extends Bag<Command> {}
    public static class ClientCommands extends EventMessage {
        public Bag<Command> commands;

        public ClientCommands() {}
        public ClientCommands(Bag<Command> commands) {
            this.commands = commands;
        }
    }

    public static class EntityData implements Message {
        public int owner;
        public EntityComponent component;

        public EntityData() {}
        public EntityData(Entity entity, EntityComponent component) {
            owner = entity.getId();
            this.component = component;
        }

        @Override
        public void receive(Connection pc) {
            Entity entity = WinterGame.gameClient.toClientEntity(owner);
            component.receive(pc, entity);
        }

        public String toString() {
            return "EntityData(" + owner + ", " + component.toString() + ")";
        }
    }

}
