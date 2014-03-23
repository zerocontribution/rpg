package io.zerocontribution.winter.spawners;

import com.artemis.World;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.utils.ServerGlobals;
import net.dermetfan.utils.libgdx.maps.MapUtils;

import java.util.List;

public abstract class Spawner {

    public final static String MAP_SPAWN_LAYER = "Spawns";

    public List<String> enemies;
    public String zone;

    abstract public void spawn(World world);

    public boolean shouldSpawn(World world) {
        return true;
    }

    protected Rectangle getSpawnBoundary(String zone) {
        MapLayer spawns = ServerGlobals.currentMap.getLayers().get(MAP_SPAWN_LAYER);
        if (spawns == null) {
            throw new RuntimeException("Cannot find Spawns map layer");
        }

        RectangleMapObject spawnZone = (RectangleMapObject) spawns.getObjects().get(zone);
        if (spawnZone == null) {
            throw new RuntimeException("Cannot find '" + zone + "' spawn zone");
        }

        return spawnZone.getRectangle();
    }

    protected Vector2 getSpawnPoint(Rectangle r) {
        float x, y;
        do {
            x = r.getX() + r.getWidth() * (float) Math.random();
            y = r.getY() + r.getHeight() * (float) Math.random();
        } while (!r.contains(x, y));

        Vector2 location = MapUtils.toIsometricGridPoint(
                new Vector2(x, y),
                ServerGlobals.currentMap.getProperties().get("tilewidth", Integer.class),
                ServerGlobals.currentMap.getProperties().get("tileheight", Integer.class));

        // TODO HACK: The math is totally incorrect here... so I need to clamp it to valid map
        // locations. Will need to revisit this.
        if (location.x < 1) {
            location.x = 1;
        }
        if (location.y < 1) {
            location.y = 1;
        }

        return location;
    }

    protected String getGroupName() {
        return this.getClass().getSimpleName() + this.hashCode();
    }

}
