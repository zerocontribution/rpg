package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.Pair;
import io.zerocontribution.winter.components.*;

public class CollisionSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Bounds> boundsMapper;

    @Mapper
    ComponentMapper<Velocity> velocityMapper;

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<Dimensions> dimensionsMapper;

    @Mapper
    ComponentMapper<PairMap> pairMapMapper;

    private ImmutableBag<Entity> actors;

    private Entity view;

    @SuppressWarnings("unchecked")
    public CollisionSystem() {
        super(Aspect.getAspectForAll(Blocking.class, Bounds.class));
    }

    @Override
    protected void initialize() {
        view = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
        addCollisionTiles();
    }

    private void addCollisionTiles() {
        PairMap pairMap = pairMapMapper.get(view);

        TiledMap map = Assets.currentMap;
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("Ground");

        float mapW = (float) map.getProperties().get("width", Integer.class);
        float mapH = (float) map.getProperties().get("height", Integer.class);

        // TODO Add -x,-y grid coordinates so they can be added to blocking tiles.
        // TODO Rotate bounding boxes for blocks 90*
        // TODO ??? Default to squares; check for texture region?
        for (int y = 0; y <= mapH; y++) {
            for (int x = 0; x <= mapW; x++) {
                if (pairMap.map.get(Pair.get(x, y)) == null) {
                    TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                    if (cell == null || cell.getTile().getProperties().containsKey("obstacle")) {
                        EntityFactory.createBlockingTile(world, x, y).addToWorld();
                        pairMap.map.put(Pair.get(x, y), new Boolean(true));
                    }
                }
            }
        }
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        actors = world.getManager(GroupManager.class).getEntities(Constants.Groups.ACTORS);

        for (int i = 0; i < actors.size(); i++) {
            Entity actor = actors.get(i);
            Velocity actorVelocity = velocityMapper.get(actor);

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

    private Bounds applyVelocity(float xVel, float yVel, Bounds bounds) {
        Bounds tempBounds = new Bounds();
        tempBounds.rect = new Rectangle(bounds.rect);
        tempBounds.rect.x += xVel * world.getDelta();
        tempBounds.rect.y += yVel * world.getDelta();
        return tempBounds;
    }

    private void checkCollisionWithActor(Entity actor, Entity entity, Bounds xBounds, Bounds yBounds, Bounds xyBounds, Velocity actorVelocity) {
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