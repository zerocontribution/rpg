package io.zerocontribution.winter.spawners;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.components.Condition;

/**
 * Will spawn enemies if:
 *
 * 1. The spawn interval has elapsed
 * 2. The spawn interval has not elapsed, but there are no more enemies alive created by this Spawner
 *
 * This process will continue until all players are dead.
 */
public class WaveSpawner extends IntervalSpawner {

    @Override
    public boolean shouldSpawn(World world) {
        boolean flag = super.shouldSpawn(world);

        if (!flag) {
            flag = !anyAlive(world.getManager(GroupManager.class).getEntities(getGroupName()));
        }

        if (flag) {
                flag = anyAlive(world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS));
        }

        return flag;
    }

    protected boolean anyAlive(ImmutableBag<Entity> entities) {
        boolean alive = false;
        for (int i = 0; i < entities.size(); i++) {
            Condition condition = entities.get(i).getComponent(Condition.class);
            if (condition != null && condition.state != State.DYING) {
                alive = true;
                break;
            }
        }
        return alive;
    }

}
