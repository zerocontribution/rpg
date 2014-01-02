package io.zerocontribution.winter.ai.modules;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;

/**
 * Attacks first closest player.
 *
 * @todo Random selection from all players within range.
 */
public class BasicAttack extends AbstractAIModule {

    ComponentMapper<Position> positionMapper;

    ComponentMapper<Stats> statsMapper;

    ComponentMapper<Actor> actorMapper;

    ComponentMapper<Condition> conditionMapper;

    private float range, cooldown, timer;

    public BasicAttack(World world, float range, float cooldown) {
        super(world);
        this.range = range;
        this.cooldown = cooldown;
        this.timer = 0;
    }

    @Override
    public void initialize() {
        positionMapper = world.getMapper(Position.class);
        statsMapper = world.getMapper(Stats.class);
        actorMapper = world.getMapper(Actor.class);
        conditionMapper = world.getMapper(Condition.class);
    }

    // TODO Should probably check state before kicking everything off, yes?
    @Override
    public boolean process(Entity e) {
        if (conditionMapper.get(e).state == State.DYING) {
            return true;
        }

        ImmutableBag<Entity> players = world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS);
        if (players.size() == 0) {
            GdxLogHelper.log("ai-attack", "No players to look for: Exiting AI processor");
            return true;
        }

        timer -= world.delta;
        if (timer > 0) {
            return false;
        }

        timer = cooldown;

        Position entityPosition = positionMapper.get(e);

        Entity target = selectTarget(players, entityPosition);

        // No targets within range.
        // TODO We should make sure the attack isn't an AOE!
        if (target != null) {
            GdxLogHelper.log("ai-attack", "Target found: attacking");
            Actor actor = actorMapper.get(e);
            e.addComponent(new ActionInput(Constants.Groups.NPC_ATTACKS, actor.primaryAttack, target));
            e.changedInWorld();
        }

        return false;
    }

    private Entity selectTarget(ImmutableBag<Entity> players, Position entityPosition) {
        for (int i = 0; i < players.size(); i++) {
            Position playerPosition = positionMapper.get(players.get(i));
            Condition playerCondition = conditionMapper.get(players.get(i));

            if (playerCondition.state == State.DYING) {
                continue;
            }

            float dst = (float) Math.hypot(
                    Math.abs(entityPosition.x - playerPosition.x),
                    Math.abs(entityPosition.y - playerPosition.y));

            if (dst < range) {
                return players.get(i);
            }
        }

        return null;
    }
}
