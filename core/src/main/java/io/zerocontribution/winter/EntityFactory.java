package io.zerocontribution.winter;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.MapHelper;

public class EntityFactory {

    public static Entity createPlayer(World world, float x, float y) {
        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(x, y);

        e.addComponent(new Name("player"));

        e.addComponent(new Facing(Directions.DOWN));

        e.addComponent(new Condition(State.RUN));

        Bounds bounds = new Bounds();
        bounds.rect = new Rectangle();
        bounds.rect.x = worldVector.x;
        bounds.rect.y = worldVector.y;
        bounds.rect.width = 18;
        bounds.rect.height = 64; // TODO This is definitely not correct.
        e.addComponent(bounds);

        e.addComponent(new Blocking());

        e.addComponent(new Position(worldVector.x, worldVector.y));

        e.addComponent(new Dimensions(18, 64));

        e.addComponent(new Velocity());

        e.addComponent(new AnimationName(Constants.Animations.Player.RUN_DOWN));

        e.addComponent(new AnimationTimer(0f));

        e.addComponent(new Player());

        world.getManager(TagManager.class).register(Constants.Tags.PLAYER, e);
        world.getManager(GroupManager.class).add(e, Constants.Groups.ACTORS);

        return e;
    }

    // TODO Add Spawner & Despawner Systems
    public static Entity createEnemy(World world, float x, float y) {
        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(x, y);

        e.addComponent(new Name("player")); // TODO Change when more assets come along.

        e.addComponent(new SpriteColor(1, 0.3f, 0.3f, 1));

        e.addComponent(new Facing(Directions.DOWN));

        e.addComponent(new Condition(State.RUN));

        Bounds bounds = new Bounds();
        bounds.rect = new Rectangle();
        bounds.rect.x = worldVector.x;
        bounds.rect.y = worldVector.y;
        bounds.rect.width = 18;
        bounds.rect.height = 64;
        e.addComponent(bounds);

        e.addComponent(new Blocking());

        e.addComponent(new Position(worldVector.x, worldVector.y));

        e.addComponent(new Dimensions(18, 64));

        e.addComponent(new Velocity());

        e.addComponent(new AnimationName(Constants.Animations.Player.RUN_DOWN)); // TODO Change with new assets

        e.addComponent(new AnimationTimer(0f));

        world.getManager(TagManager.class).register(Constants.Tags.ENEMY, e);
        world.getManager(GroupManager.class).add(e, Constants.Groups.ACTORS);

        return e;
    }

    public static Entity createMap(World world, SpriteBatch spriteBatch) {
        Entity e = world.createEntity();

        Cam cam = new Cam();
        cam.camera = new OrthographicCamera(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        cam.camera.position.x = Constants.CAMERA_WIDTH / 2;
        cam.camera.position.y = Constants.CAMERA_HEIGHT / 2;
        cam.camera.update();
        spriteBatch.setProjectionMatrix(cam.camera.combined);
        e.addComponent(cam);

        MapView view = new MapView();
        view.renderer = new IsometricTiledMapRenderer(Assets.currentMap, spriteBatch);
        e.addComponent(view);

        e.addComponent(new PairMap());

        world.getManager(TagManager.class).register(Constants.Tags.VIEW, e);

        return e;
    }

    public static Entity createBlockingTile(World world, float x, float y) {
        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(x, y);

        e.addComponent(new Position(worldVector.x, worldVector.y));

        GridPosition gridPosition = new GridPosition();
        gridPosition.x = x;
        gridPosition.y = y;
        e.addComponent(gridPosition);

        Dimensions dimensions = new Dimensions(Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
        e.addComponent(dimensions);

        Bounds bounds = new Bounds();
        bounds.rect = new Rectangle();
        bounds.rect.x = worldVector.x;
        bounds.rect.y = worldVector.y;
        bounds.rect.width = Constants.TILE_WIDTH;
        bounds.rect.height = Constants.TILE_HEIGHT;
        e.addComponent(bounds);

        e.addComponent(new Blocking());

        world.getManager(GroupManager.class).add(e, Constants.Groups.BLOCKING_TILES);

        return e;
    }

}
