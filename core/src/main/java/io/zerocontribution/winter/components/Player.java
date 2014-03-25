package io.zerocontribution.winter.components;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;

public class Player extends EntityComponent {
    public String name;

    public Player() {}
    public Player(String name) {
        this.name = name;
    }

    @Override
    public void receive(Connection pc, Entity entity) {
        super.receive(pc, entity);

        WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.CLIENT);
        WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.PLAYERS);
        WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.ACTORS);
    }

}
