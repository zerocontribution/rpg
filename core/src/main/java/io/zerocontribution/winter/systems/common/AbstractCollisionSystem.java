package io.zerocontribution.winter.systems.common;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Rectangle;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.*;

public abstract class AbstractCollisionSystem extends EntitySystem {

    ComponentMapper<Velocity> velocityMapper;
    ComponentMapper<Bounds> boundsMapper;
    ComponentMapper<Position> positionMapper;
    ComponentMapper<Dimensions> dimensionsMapper;

    private boolean collisionTilesAdded = false;

    @SuppressWarnings("unchecked")
    public AbstractCollisionSystem() {
        super(Aspect.getAspectForAll(Blocking.class, Bounds.class));
    }

    @Override
    protected void initialize() {
        // Apparently I can't inject mappers in abstract classes...
        velocityMapper = world.getMapper(Velocity.class);
        boundsMapper = world.getMapper(Bounds.class);
        positionMapper = world.getMapper(Position.class);
        dimensionsMapper = world.getMapper(Dimensions.class);
    }

    abstract protected void addCollisionTiles();

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        // Server-side starts running the systems before the map is loaded; so this can't be done on init anymore.
        if (!collisionTilesAdded) {
            addCollisionTiles();
            collisionTilesAdded = true;
        }

        ImmutableBag<Entity> actors = world.getManager(GroupManager.class).getEntities(Constants.Groups.ACTORS);

        for (int i = 0; i < actors.size(); i++) {
            Entity actor = actors.get(i);
            Velocity actorVelocity = velocityMapper.getSafe(actor);

            if (actorVelocity.x != 0f || actorVelocity.y != 0f) {
                Bounds bounds = boundsMapper.get(actor);

                for (int j = 0; j < entities.size(); j++) {
                    Entity e = entities.get(j);

                    if (!e.equals(actor)) {
                        Bounds xBounds = applyVelocity(actorVelocity.x, 0, bounds);
                        Bounds yBounds = applyVelocity(0, actorVelocity.y, bounds);
                        Bounds xyBounds = applyVelocity(actorVelocity.x, actorVelocity.y, bounds);

                        checkCollisionWithActor(actor, entities.get(j), xBounds, yBounds, xyBounds, actorVelocity);
                    }
                }
            }
        }
    }

    protected Bounds applyVelocity(float xVel, float yVel, Bounds bounds) {
        Bounds tempBounds = new Bounds();
        tempBounds.rect = new Rectangle(bounds.rect);
        tempBounds.rect.x += xVel * world.getDelta();
        tempBounds.rect.y += yVel * world.getDelta();
        return tempBounds;
    }

    protected void checkCollisionWithActor(Entity actor, Entity entity, Bounds xBounds, Bounds yBounds, Bounds xyBounds, Velocity actorVelocity) {
        Bounds blockingBounds = boundsMapper.get(entity);
        Position actorPosition = positionMapper.get(actor);
        Dimensions actorDimensions = dimensionsMapper.get(actor);

        if (!xyBounds.rect.overlaps(blockingBounds.rect)) {
            return;
        } else if (!xBounds.rect.overlaps(blockingBounds.rect)) {
            if (actorVelocity.y < 0f) {
                actorPosition.y = blockingBounds.rect.y + blockingBounds.rect.height;
            } else if (actorVelocity.y > 0f) {
                actorPosition.y = blockingBounds.rect.y - actorDimensions.height;
            }
            actorVelocity.y = 0f;
        } else if (!yBounds.rect.overlaps(blockingBounds.rect)) {
            if (actorVelocity.x < 0f) {
                actorPosition.x = blockingBounds.rect.x + blockingBounds.rect.width;
            } else if (actorVelocity.x > 0f) {
                actorPosition.x = blockingBounds.rect.x - actorDimensions.width;
            }
            actorVelocity.x = 0f;
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
