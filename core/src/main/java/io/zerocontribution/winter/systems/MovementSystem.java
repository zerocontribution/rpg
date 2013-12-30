package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import io.zerocontribution.winter.Directions;
import io.zerocontribution.winter.components.Bounds;
import io.zerocontribution.winter.components.Facing;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.components.Velocity;

public class MovementSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<Velocity> velocityMapper;

    @Mapper
    ComponentMapper<Bounds> boundsMapper;

    @Mapper
    ComponentMapper<Facing> facingMapper;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class, Bounds.class));
    }

    protected void process(Entity e) {
        Position position = positionMapper.get(e);
        Velocity velocity = velocityMapper.get(e);
        Bounds bounds = boundsMapper.get(e);

        position.x += velocity.x * world.delta;
        position.y += velocity.y * world.delta;
        bounds.rect.x = position.x;
        bounds.rect.y = position.y;

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
