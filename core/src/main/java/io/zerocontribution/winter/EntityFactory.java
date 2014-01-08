package io.zerocontribution.winter;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.ai.AI;
import io.zerocontribution.winter.ai.normals.ZombieAI;
import io.zerocontribution.winter.assets.EnemyAsset;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;

import java.lang.reflect.InvocationTargetException;

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

        Actor actor = new Actor();
        actor.abilities.put(1, new Delay(1));
        e.addComponent(actor);

        e.addComponent(new Stats(
                100,
                100,
                100,
                100,
                0,
                1,
                1,
                1,
                1,
                1
        ));

        world.getManager(TagManager.class).register(Constants.Tags.PLAYER, e);
        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);
        world.getManager(GroupManager.class).add(e, Constants.Groups.ACTORS);

        return e;
    }

    public static Entity createEnemy(World world, String name, float x, float y) {
        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(x, y);

        EnemyAsset enemyAsset = Assets.enemies.get(name);
        GdxLogHelper.log("asset", "Creating enemy " + enemyAsset);

        e.addComponent(new Name(enemyAsset.name));

        if (enemyAsset.color.length == 4) {
            e.addComponent(new SpriteColor(
                    enemyAsset.color[0],
                    enemyAsset.color[1],
                    enemyAsset.color[2],
                    enemyAsset.color[3]));
        }

        e.addComponent(new Facing(Directions.DOWN));

        e.addComponent(new Condition(State.RUN));

        // TODO Do we want to set boundaries towards the bottom of the entity? Might look better.
        Bounds bounds = new Bounds();
        bounds.rect = new Rectangle();
        bounds.rect.x = worldVector.x;
        bounds.rect.y = worldVector.y;
        bounds.rect.width = enemyAsset.bounds[0];
        bounds.rect.height = enemyAsset.bounds[1];
        e.addComponent(bounds);

        e.addComponent(new Blocking());

        e.addComponent(new Position(worldVector.x, worldVector.y));

        e.addComponent(new GridPosition(x, y));

        e.addComponent(new TargetGridPosition(x, y));

        e.addComponent(new Dimensions(enemyAsset.bounds[0], enemyAsset.bounds[1]));

        e.addComponent(new Velocity());

        // TODO Change with new assets
        // TODO Animation names should be dynamically resolved using the name component, facing & condition.
        e.addComponent(new AnimationName(Constants.Animations.Player.RUN_DOWN));

        e.addComponent(new AnimationTimer(0f));

        AI ai = null;
        try {
            ai = (AI) Class.forName(enemyAsset.aiClass).getConstructor(World.class).newInstance(world);
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        e.addComponent(new Npc(ai));

        Actor actor = new Actor();
        for (int abilityId : enemyAsset.abilities) {
            Ability ability = Assets.abilities.get(abilityId);
            actor.abilities.put(abilityId, new Delay((long) ability.cooldown));
        }
        e.addComponent(actor);

        int health = enemyAsset.baseStats.get("health") == null ? 100 : enemyAsset.baseStats.get("health");
        int power = enemyAsset.baseStats.get("power") == null ? 100 : enemyAsset.baseStats.get("power");
        e.addComponent(new Stats(
                health, power, health, power,
                (enemyAsset.baseStats.get("experience") == null ? 0 : enemyAsset.baseStats.get("experience")),
                (enemyAsset.baseStats.get("level") == null ? 1 : enemyAsset.baseStats.get("level")),
                (enemyAsset.baseStats.get("technicalAbility") == null ? 10 : enemyAsset.baseStats.get("technicalAbility")),
                (enemyAsset.baseStats.get("cool") == null ? 10 : enemyAsset.baseStats.get("cool")),
                (enemyAsset.baseStats.get("attractiveness") == null ? 10 : enemyAsset.baseStats.get("attractiveness")),
                (enemyAsset.baseStats.get("body") == null ? 10 : enemyAsset.baseStats.get("body"))
        ));

        world.getManager(GroupManager.class).add(e, Constants.Groups.ENEMIES);
        world.getManager(GroupManager.class).add(e, Constants.Groups.ACTORS);

        return e;
    }

    // TODO Add Spawner & Despawner Systems
    public static Entity createEnemy(World world, float x, float y) {
        GdxLogHelper.log("assets", "Deprecated createEnemy method");

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

        // TODO Maybe all Position information should be rolled into a singular Component?
        e.addComponent(new GridPosition(x, y));

        e.addComponent(new TargetGridPosition(x, y));

        e.addComponent(new Dimensions(18, 64));

        e.addComponent(new Velocity());

        e.addComponent(new AnimationName(Constants.Animations.Player.RUN_DOWN)); // TODO Change with new assets

        e.addComponent(new AnimationTimer(0f));

        e.addComponent(new Npc(new ZombieAI(world)));

        Actor actor = new Actor();
        actor.abilities.put(1, new Delay(1));
        e.addComponent(actor);

        e.addComponent(new Stats(
                100,
                100,
                100,
                100,
                0,
                1,
                1,
                1,
                1,
                1
        ));

        world.getManager(GroupManager.class).add(e, Constants.Groups.ENEMIES);
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
