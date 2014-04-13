package io.zerocontribution.winter.server;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.AbstractEntityFactory;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.ai.AIRegistry;
import io.zerocontribution.winter.ai.normals.ZombieAI;
import io.zerocontribution.winter.assets.EnemyAsset;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.struct.Delay;
import io.zerocontribution.winter.struct.Directions;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;
import io.zerocontribution.winter.utils.ServerGlobals;

public class ServerEntityFactory extends AbstractEntityFactory {

    public Entity createPlayer(World world, String name) {
        Entity e = world.createEntity();

        e.addComponent(new Player(name));

        // TODO: Server depends on CLIENT & LOCAL_PLAYER. :\
        world.getManager(TagManager.class).register(Constants.Tags.LOCAL_PLAYER, e);
        world.getManager(GroupManager.class).add(e, Constants.Groups.CLIENT);
        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);
        world.getManager(GroupManager.class).add(e, Constants.Groups.ACTORS);

        return e;
    }

    public void createWorldPlayer(Entity e, float x, float y) {
        Vector2 worldVector = MapHelper.gridToWorld(ServerGlobals.currentMap, x, y);

        e.addComponent(new Name("player"));

        e.addComponent(new Facing(Directions.DOWN));

        e.addComponent(new Condition(State.RUN));

        Bounds bounds = new Bounds();
        bounds.rect = new Rectangle();
        bounds.rect.x = worldVector.x;
        bounds.rect.y = worldVector.y;
        bounds.rect.width = 18;
        bounds.rect.height = 24;
        e.addComponent(bounds);

        e.addComponent(new Blocking());

        e.addComponent(new Position(worldVector.x, worldVector.y));

        e.addComponent(new Dimensions(18, 64));

        e.addComponent(new Velocity());

        e.addComponent(new AnimationName(Constants.Animations.Player.RUN_DOWN));

        e.addComponent(new AnimationTimer(0f));

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
    }

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

    // TODO Add Spawner & Despawner Systems
    public Entity createEnemy(World world, float x, float y) {
        GdxLogHelper.log("assets", "Deprecated createEnemy method");

        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(ServerGlobals.currentMap, x, y);

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

        e.addComponent(new Npc(new AIRegistry().getNew(ZombieAI.NAME, world)));

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

    public Entity createEnemy(World world, String name, float x, float y) {
        Entity e = world.createEntity();
        Vector2 worldVector = MapHelper.gridToWorld(ServerGlobals.currentMap, x, y);

        Log.info("Factory", worldVector.toString());

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

        e.addComponent(new Npc(new AIRegistry().getNew(enemyAsset.ai, world)));

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

}
