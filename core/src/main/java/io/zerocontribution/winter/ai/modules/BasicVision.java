package io.zerocontribution.winter.ai.modules;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;
import io.zerocontribution.winter.utils.ServerGlobals;

/**
 * @todo Written for multiple players, but the BasicFollow system only expects a single player. Will need a way to
 *               track this vision state across AI modules.
 */
public class BasicVision extends AbstractAIModule {

    ComponentMapper<Position> positionMapper;

    ComponentMapper<Velocity> velocityMapper;

    ComponentMapper<Condition> conditionMapper;

    ComponentMapper<TargetGridPosition> targetGridPositionMapper;

    private final float range;

    private final float stopRange;

    private boolean spotted;

    /**
     * @param world
     * @param range Position range (pixels)
     */
    public BasicVision(World world, float range, float stopRange) {
        super(world);
        this.spotted = false;
        this.range = range;
        this.stopRange = stopRange;
    }

    @Override
    public void initialize() {
        positionMapper = world.getMapper(Position.class);
        velocityMapper = world.getMapper(Velocity.class);
        conditionMapper = world.getMapper(Condition.class);
        targetGridPositionMapper = world.getMapper(TargetGridPosition.class);
    }

    @Override
    public boolean process(Entity e) {
        if (conditionMapper.get(e).state == State.DYING) {
            return true;
        }

        ImmutableBag<Entity> players = world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS);

        for (int i = 0; i < players.size(); i++) {
            if (conditionMapper.get(players.get(i)).state == State.DYING) {
                continue;
            }
            
            Position entityPosition = positionMapper.get(e);
            Position playerPosition = positionMapper.get(players.get(i));
            TargetGridPosition targetPosition = targetGridPositionMapper.get(e);

            float dst = (float) Math.hypot(
                    Math.abs(entityPosition.x - playerPosition.x),
                    Math.abs(entityPosition.y - playerPosition.y));

            if (!spotted) {
                if (dst < range) {
                    Vector2 gridPosition = MapHelper.worldToGrid(playerPosition.x, playerPosition.y);
                    targetPosition.x = gridPosition.x;
                    targetPosition.y = gridPosition.y;

                    spotted = true;
                    return false;
                } else {
                    targetPosition.x = -1;
                    targetPosition.y = -1;
                    return true;
                }
            } else {
                if (dst > stopRange) {
                    spotted = false;

                    Velocity velocity = velocityMapper.get(e);
                    velocity.set(0, 0);
                    e.addComponent(new Update());
                    e.changedInWorld();

                    targetPosition.x = -1;
                    targetPosition.y = -1;
                    return true;
                } else {
                    Vector2 gridPosition = MapHelper.worldToGrid(ServerGlobals.currentMap, playerPosition.x, playerPosition.y);
                    targetPosition.x = gridPosition.x;
                    targetPosition.y = gridPosition.y;
                    
                    return false;
                }
            }
        }

        return true;
    }
}
