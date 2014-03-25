package io.zerocontribution.winter.components;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.ai.AI;

public class Npc extends EntityComponent {
    public Type type = Type.ENEMY;

    transient public AI ai;

    public enum Type {
        FRIENDLY,
        ENEMY
    }

    public Npc() {}
    public Npc(AI ai) {
        this.ai = ai;
    }

    @Override
    public void receive(Connection pc, Entity entity) {
        super.receive(pc, entity);

        WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.ACTORS);
        if (type == Type.ENEMY) {
            WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.ENEMIES);
        }
    }

}
