package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.struct.Directions;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.MapHelper;
import io.zerocontribution.winter.utils.ServerGlobals;

public class MovementSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<GridPosition> gridPositionMapper;

    @Mapper
    ComponentMapper<Velocity> velocityMapper;

    @Mapper
    ComponentMapper<Bounds> boundsMapper;

    @Mapper
    ComponentMapper<Facing> facingMapper;

    Map currentMap;

    @SuppressWarnings("unchecked")
    public MovementSystem(Map currentMap) {
        super(Aspect.getAspectForAll(Position.class, Velocity.class, Bounds.class));
        this.currentMap = currentMap;
    }

    @Override
    protected boolean checkProcessing() {
        // HACK: Server doesn't immediately initialize the map.
        if (currentMap == null) {
            currentMap = ServerGlobals.currentMap;
            return false;
        }
        return true;
    }

    protected void process(Entity e) {
        Position position = positionMapper.get(e);
        Velocity velocity = velocityMapper.get(e);
        Bounds bounds = boundsMapper.get(e);

        if (velocity.x != 0 || velocity.y != 0) {
            position.move(velocity.x * world.delta, velocity.y * world.delta);
            bounds.rect.x = position.x;
            bounds.rect.y = position.y;

            Vector2 grid = MapHelper.worldToGrid(currentMap, position.x, position.y);
            if (gridPositionMapper.has(e)) {
                GridPosition gridPosition = gridPositionMapper.get(e);
                gridPosition.x = grid.x;
                gridPosition.y = grid.y;
            }

            if (facingMapper.has(e)) {
                Facing facing = facingMapper.get(e);

                if (velocity.x > 0 && velocity.y == 0) {
                    facing.direction = Directions.RIGHT;
                } else if (velocity.x < 0 && velocity.y == 0) {
                    facing.direction = Directions.LEFT;
                } else if (velocity.x > 0 && velocity.y > 0) {
                    facing.direction = Directions.UP_RIGHT;
                } else if (velocity.x < 0 && velocity.y > 0) {
                    facing.direction = Directions.UP_LEFT;
                } else if (velocity.x > 0 && velocity.y < 0) {
                    facing.direction = Directions.DOWN_RIGHT;
                } else if (velocity.x < 0 && velocity.y < 0) {
                    facing.direction = Directions.DOWN_LEFT;
                } else if (velocity.x == 0 && velocity.y > 0) {
                    facing.direction = Directions.UP;
                } else if (velocity.x == 0 && velocity.y < 0) {
                    facing.direction = Directions.DOWN;
                }
            }
        }
    }

}
