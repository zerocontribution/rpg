package io.zerocontribution.winter.ai.modules;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.ai.pathfinding.*;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.ClientGlobals;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;
import io.zerocontribution.winter.utils.ServerGlobals;

/**
 * @todo Remove Mover class.
 * @todo The follower never reaches the target destination beacuse the target is in that spot, so the AI keeps
 *       recalculating the final step. I presume this won't be as big of a deal since they'll be dying.
 * @todo Add minimum distance for "follower" functionality.
 * @todo Add "stuck" resolution: If entity hasn't changed position in n ticks and hasn't reached its target yet,
 *       try moving to a different neighbor tile and retry.
 */
public class BasicFollow extends AbstractAIModule {

    private static Mover mover = new Mover();

    ComponentMapper<Condition> conditionMapper;

    ComponentMapper<Position> positionMapper;

    ComponentMapper<GridPosition> gridPositionMapper;

    ComponentMapper<TargetGridPosition> targetGridPositionMapper;

    ComponentMapper<Velocity> velocityMapper;

    private IsometricTileMap map;

    private int range;
    private float speed;

    private int recalculateInterval = 20; // TODO Force recalculations until collisions work better.

    private int interval = 0;

    public BasicFollow(World world, int range, float speed) {
        super(world);
        this.range = range;
        this.speed = speed;
    }

    public BasicFollow(World world, int range, float speed, int recalculateInterval) {
        this(world, range, speed);
        this.recalculateInterval = recalculateInterval;
    }

    @Override
    public void initialize() {
        conditionMapper = world.getMapper(Condition.class);
        positionMapper = world.getMapper(Position.class);
        gridPositionMapper = world.getMapper(GridPosition.class);
        targetGridPositionMapper = world.getMapper(TargetGridPosition.class);
        velocityMapper = world.getMapper(Velocity.class);

        Entity view = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
        map = new IsometricTileMap(ServerGlobals.currentMap, world.getMapper(PairMap.class).get(view));
    }

    @Override
    public boolean process(Entity e) {
        if (conditionMapper.get(e).state == State.DYING) {
            return true;
        }

        TargetGridPosition targetPosition = targetGridPositionMapper.get(e);
        if (targetPosition.x == -1 && targetPosition.y == -1) {
            return false;
        }

        GridPosition gridPosition = gridPositionMapper.get(e);
        Velocity velocity = velocityMapper.get(e);

        if (gridPosition.x == targetPosition.x && gridPosition.y == targetPosition.y) {
            targetPosition.path = null;
            targetPosition.currentPathStep = 1; // Step 0 is starting point.
            interval = 0;

            velocity.x = 0f;
            velocity.y = 0f;
        } else if (targetPosition.y >= 0 && targetPosition.x >= 0) {
            if (targetPosition.pathFinder == null) {
                targetPosition.pathFinder = new AStarPathFinder(map, range, true, new ManhattanHeuristic());
            }

            boolean shouldRecalculate = false;
            if (recalculateInterval > 0) {
                interval++;
                shouldRecalculate = interval % recalculateInterval == 0;
                if (gridPosition.x < 0 || gridPosition.y < 0) {
                    shouldRecalculate = false;
                }
            }

            if (targetPosition.path == null || shouldRecalculate) {
                // TODO Refactor pathfinding code to not require the Mover class.
                targetPosition.path = targetPosition.pathFinder.findPath(
                        mover,
                        (int) gridPosition.x,
                        (int) gridPosition.y,
                        (int) targetPosition.x,
                        (int) targetPosition.y);
                targetPosition.currentPathStep = 1;
                interval = 0;
            }

            if (targetPosition.path == null) {
                GdxLogHelper.error("pathfinding", new StringBuilder()
                        .append("Could not find path for ")
                        .append((int) gridPosition.x).append(",").append((int) gridPosition.y)
                        .append(" to ")
                        .append((int) targetPosition.x).append(",").append((int) targetPosition.y)
                        .toString());

                velocity.x = 0;
                velocity.y = 0;
            } else {
                if (targetPosition.path.getLength() == targetPosition.currentPathStep) {
                    // Reached our destination. Call it a day.
                    targetPosition.path = null;
                    targetPosition.currentPathStep = 1;
                    velocity.x = 0;
                    velocity.y = 0;
                } else {
                    Path.Step step = targetPosition.path.getStep(targetPosition.currentPathStep);

                    if (step.getX() > gridPosition.x) {
                        velocity.x = speed;
                    } else if (step.getX() < gridPosition.x) {
                        velocity.x = -speed;
                    } else {
                        velocity.x = 0;
                    }

                    if (step.getY() > gridPosition.y) {
                        velocity.y = speed;
                    } else if (step.getY() < gridPosition.y) {
                        velocity.y = -speed;
                    } else {
                        velocity.y = 0;
                    }

                    if (velocity.x == 0f && velocity.y == 0) {
                        targetPosition.currentPathStep++;
                    }
                }
            }
        } else {
            // An invalid target destination that we're just going to ignore.
            // TODO Fix the collision system so negative coordinates are not possible.
            GdxLogHelper.log("pathfinding", "TODO invalid destination");
        }

        return false;
    }

}
