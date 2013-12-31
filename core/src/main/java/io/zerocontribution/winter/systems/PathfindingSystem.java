package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.ai.pathfinding.AStarPathFinder;
import io.zerocontribution.winter.ai.pathfinding.IsometricTileMap;
import io.zerocontribution.winter.ai.pathfinding.Mover;
import io.zerocontribution.winter.ai.pathfinding.Path;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;

/**
 * @todo Move pathfinding system into AI modules? Pathfinder ranges should be configurable by NPC type.
 */
public class PathfindingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<GridPosition> gridPositionMapper;

    @Mapper
    ComponentMapper<TargetGridPosition> targetGridPositionMapper;

    @Mapper
    ComponentMapper<Velocity> velocityMapper;

    @Mapper
    ComponentMapper<PairMap> pairMapMapper;

    private Mover mover = new Mover();

    private Entity view;

    private Entity player;

    private IsometricTileMap map;

    @SuppressWarnings("unchecked")
    public PathfindingSystem() {
        super(Aspect.getAspectForAll(Npc.class, Velocity.class, Position.class, GridPosition.class, TargetGridPosition.class));
    }

    @Override
    protected void initialize() {
        view = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
        map = new IsometricTileMap(Assets.currentMap, pairMapMapper.get(view));
        player = world.getManager(TagManager.class).getEntity(Constants.Tags.PLAYER);
    }

    @Override
    protected void process(Entity e) {
        GridPosition gridPosition = gridPositionMapper.get(e);
        TargetGridPosition targetPosition = targetGridPositionMapper.get(e);
        Velocity velocity = velocityMapper.get(e);

        Position position = positionMapper.get(player);
        Vector2 playerPosition = MapHelper.worldToGrid(position.x, position.y);
        targetPosition.x = playerPosition.x;
        targetPosition.y = playerPosition.y;

        if (gridPosition.x == targetPosition.x && gridPosition.y == targetPosition.y) {
            targetPosition.path = null;
            targetPosition.currentPathStep = 1; // Step 0 is starting point.

            velocity.x = 0f;
            velocity.y = 0f;
        } else if (targetPosition.y >= 0 && targetPosition.x >= 0) {
            if (targetPosition.pathFinder == null) {
                targetPosition.pathFinder = new AStarPathFinder(map, 20, true);
            }

            if (targetPosition.path == null) {
                // TODO Refactor pathfinding code to not require the Mover class.
                targetPosition.path = targetPosition.pathFinder.findPath(
                        mover,
                        (int) gridPosition.x,
                        (int) gridPosition.y,
                        (int) targetPosition.x,
                        (int) targetPosition.y);
            }

            if (targetPosition.path == null) {
                GdxLogHelper.log("pathfinding", new StringBuilder()
                        .append("Could not find path for ")
                        .append(gridPosition.x).append(",").append(gridPosition.y)
                        .append(" to ")
                        .append(targetPosition.x).append(",").append(targetPosition.y)
                        .toString());

                velocity.x = 0f;
                velocity.y = 0f;
            } else {
                if (targetPosition.path.getLength() == targetPosition.currentPathStep) {
                    // Reached our destination. Call it a day.
                    targetPosition.path = null;
                    targetPosition.currentPathStep = 1;
                    return;
                }

                // TODO Velocity speed must be configurable!
                Path.Step step = targetPosition.path.getStep(targetPosition.currentPathStep);

                if (step.getX() > gridPosition.x) {
                    velocity.x = 100f;
                } else if (step.getX() < gridPosition.x) {
                    velocity.x = -100f;
                } else {
                    velocity.x = 0;
                }

                if (step.getY() > gridPosition.y) {
                    velocity.y = 100f;
                } else if (step.getY() < gridPosition.y) {
                    velocity.y = -100f;
                } else {
                    velocity.y = 0;
                }

                if (velocity.x == 0f && velocity.y == 0) {
                    targetPosition.currentPathStep++;
                }
            }
        } else {
            // An invalid target destination that we're just going to ignore.
            // TODO Fix the collision system so negative coordinates are not possible.
            GdxLogHelper.log("pathfinding", "TODO invalid destination");
        }
    }

}
