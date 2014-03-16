package io.zerocontribution.winter.server;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.AbstractEntityFactory;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.MapHelper;
import io.zerocontribution.winter.utils.ServerGlobals;

public class ServerEntityFactory extends AbstractEntityFactory {

    public Entity createMap(World world) {
        Entity e = world.createEntity();
        e.addComponent(new MapView());
        e.addComponent(new PairMap());
        world.getManager(TagManager.class).register(Constants.Tags.VIEW, e);

        return e;
    }

    public Entity createBlockingTile(World world, float x, float y) {
        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(ServerGlobals.currentMap, x, y);

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
