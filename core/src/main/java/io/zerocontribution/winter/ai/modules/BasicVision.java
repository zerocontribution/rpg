package io.zerocontribution.winter.ai.modules;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.components.Condition;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.components.Velocity;
import io.zerocontribution.winter.utils.GdxLogHelper;

/**
 * @todo Written for multiple players, but the BasicFollow system only expects a single player. Will need a way to
 *               track this vision state across AI modules.
 */
public class BasicVision extends AbstractAIModule {

    ComponentMapper<Position> positionMapper;

    ComponentMapper<Velocity> velocityMapper;

    ComponentMapper<Condition> conditionMapper;

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
    }

    @Override
    public boolean process(Entity e) {
        if (conditionMapper.get(e).state == State.DYING) {
            return true;
        }

        ImmutableBag<Entity> players = world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS);

        for (int i = 0; i < players.size(); i++) {
            Position entityPosition = positionMapper.get(e);
            Position playerPosition = positionMapper.get(players.get(i));

            float dst = (float) Math.hypot(
                    Math.abs(entityPosition.x - playerPosition.x),
                    Math.abs(entityPosition.y - playerPosition.y));

            if (!spotted) {
                if (dst < range) {
                    spotted = true;
                    return false;
                } else {
                    return true;
                }
            } else {
                if (dst > stopRange) {
                    spotted = false;

                    Velocity velocity = velocityMapper.get(e);
                    velocity.x = 0;
                    velocity.y = 0;

                    return true;
                } else {
                    return false;
                }
            }
        }

        GdxLogHelper.log("ai-vision", "No players to look for: Exiting AI processor.");
        return true;
    }
}
