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
import io.zerocontribution.winter.components.*;

public class EntityFactory {

    public static Entity createPlayer(World world, float x, float y) {
        Entity e = world.createEntity();

        Bounds bounds = new Bounds();
        bounds.rect = new Rectangle();
        bounds.rect.x = x;
        bounds.rect.y = y;
        bounds.rect.width = 18;
        bounds.rect.height = 64; // TODO This is definitely not correct.
        e.addComponent(bounds);

        e.addComponent(new Blocking());

        Position position = new Position();
        position.x = x;
        position.y = y;
        e.addComponent(position);

        e.addComponent(new Dimensions(18, 64));

        e.addComponent(new Velocity());

        AnimationSprite sprite = new AnimationSprite();
        sprite.name = "down";
        sprite.width = 18;
        sprite.height = 64;
        sprite.frameTime = 1 / 2f;
        sprite.cols = 8;
        sprite.rows = 1;
        sprite.loop = true;
        sprite.layer = AnimationSprite.Layer.ACTORS_3;
        e.addComponent(sprite);

        e.addComponent(new Player());

        world.getManager(TagManager.class).register(Constants.Tags.PLAYER, e);
        world.getManager(GroupManager.class).add(e, Constants.Groups.ACTORS);
        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYER_AVATAR);

        return e;
    }

    public static Entity createMap(World world, SpriteBatch spriteBatch) {
        Entity e = world.createEntity();

        Cam cam = new Cam();
        cam.camera = new OrthographicCamera(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        cam.camera.position.x = Constants.CAMERA_WIDTH / 2;
        cam.camera.position.y = Constants.CAMERA_HEIGHT / 2;
        cam.camera.update();
        e.addComponent(cam);

        MapView view = new MapView();
        view.renderer = new IsometricTiledMapRenderer(Assets.currentMap, spriteBatch);
        e.addComponent(view);

        e.addComponent(new PairMap());

        world.getManager(TagManager.class).register(Constants.Tags.VIEW, e);

        return e;
    }

    public static Entity createBlockingTile(World world, float x, float y, float worldX, float worldY) {
        Entity e = world.createEntity();

        Position position = new Position();
        position.x = worldX;
        position.y = worldY;
        e.addComponent(position);

        GridPosition gridPosition = new GridPosition();
        gridPosition.x = x;
        gridPosition.y = y;
        e.addComponent(gridPosition);

        Dimensions dimensions = new Dimensions(Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
        e.addComponent(dimensions);

        Bounds bounds = new Bounds();
        bounds.rect = new Rectangle();
        bounds.rect.x = worldX;
        bounds.rect.y = worldY;
        bounds.rect.width = Constants.TILE_WIDTH;
        bounds.rect.height = Constants.TILE_HEIGHT;
        e.addComponent(bounds);

        e.addComponent(new Blocking());

        world.getManager(GroupManager.class).add(e, Constants.Groups.BLOCKING_TILES);

        return e;
    }

}
