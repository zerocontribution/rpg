package io.zerocontribution.winter.client;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.*;
import io.zerocontribution.winter.ai.AI;
import io.zerocontribution.winter.ai.normals.ZombieAI;
import io.zerocontribution.winter.assets.EnemyAsset;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.ClientGlobals;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;

import java.lang.reflect.InvocationTargetException;

public class ClientEntityFactory extends AbstractEntityFactory {

    // TODO Yuck.
    public Entity createMap(World world) {
        throw new IllegalStateException("Use createMap(World world, SpriteBatch spriteBatch) instead");
    }

    public Entity createMap(World world, SpriteBatch spriteBatch) {
        Entity e = world.createEntity();

        Cam cam = new Cam();
        cam.camera = new OrthographicCamera(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        cam.camera.position.x = Constants.CAMERA_WIDTH / 2;
        cam.camera.position.y = Constants.CAMERA_HEIGHT / 2;
        cam.camera.update();
        spriteBatch.setProjectionMatrix(cam.camera.combined);
        e.addComponent(cam);

        MapView view = new MapView();
        view.renderer = new IsometricTiledMapRenderer(ClientGlobals.currentMap, spriteBatch);
        e.addComponent(view);

        e.addComponent(new PairMap());

        world.getManager(TagManager.class).register(Constants.Tags.VIEW, e);

        return e;
    }

    public Entity createBlockingTile(World world, float x, float y) {
        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(ClientGlobals.currentMap, x, y);

        e.addComponent(new Position(worldVector.x, worldVector.y));

        e.addComponent(new GridPosition(x, y));

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
