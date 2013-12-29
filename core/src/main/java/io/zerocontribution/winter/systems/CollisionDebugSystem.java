package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.Pair;
import io.zerocontribution.winter.components.*;

/**
 * The only reason why this isn't included in the normal collision system is because the map renderer is processed
 * later, and all shapes are hidden.
 */
public class CollisionDebugSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Bounds> boundsMapper;

    @Mapper
    ComponentMapper<Velocity> velocityMapper;

    @Mapper
    ComponentMapper<Cam> camMapper;

    private ImmutableBag<Entity> actors;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    @SuppressWarnings("unchecked")
    public CollisionDebugSystem() {
        super(Aspect.getAspectForAll(Blocking.class, Bounds.class));
    }

    @Override
    protected void initialize() {
        shapeRenderer = new ShapeRenderer();
        camera = camMapper.get(world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW)).camera;
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

        drawBoundingBox(actor, xyBounds);
        drawBoundingBox(entity, blockingBounds);
    }

    private void drawBoundingBox(Entity e, Bounds bounds) {
        shapeRenderer.rect(bounds.rect.x, bounds.rect.y, bounds.rect.getWidth(), bounds.rect.getHeight());
    }

    @Override
    protected void begin() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void end() {
        shapeRenderer.end();
    }

}