package io.zerocontribution.winter.components;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;

public class Player extends EntityComponent {
    public String name;
    private String gameClass; // TODO Refactor to "selectedClass"

    public Player() {}
    public Player(String name) {
        this.name = name;
    }

    public void setGameClass(String gameClass) {
        this.gameClass = gameClass;
        setUpdated();
    }

    public boolean hasGameClass() {
        return gameClass != null;
    }

    public String getGameClass() {
        return gameClass;
    }

    @Override
    public void receive(Connection pc, Entity entity) {
        super.receive(pc, entity);

        WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.CLIENT);
        WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.PLAYERS);
        WinterGame.world.getManager(GroupManager.class).add(entity, Constants.Groups.ACTORS);
    }

}
