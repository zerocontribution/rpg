package io.zerocontribution.winter.spawners;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.components.Update;
import io.zerocontribution.winter.server.ServerEntityFactory;

public class IntervalSpawner extends Spawner {

    public int increment;
    public float interval;
    public long lastSpawn = 0;

    protected Rectangle boundary;

    @Override
    public boolean shouldSpawn(World world) {
        return lastSpawn == 0 || (System.currentTimeMillis() - lastSpawn) / 1000 >= interval;
    }

    public void spawn(World world) {
        if (boundary == null) {
            boundary = getSpawnBoundary(zone);
        }

        ServerEntityFactory factory = new ServerEntityFactory();
        for (int i = 0; i < increment; i++) {
            for (String name : enemies) {
                Vector2 location = getSpawnPoint(boundary);

                Entity e = factory.createEnemy(
                        world,
                        name,
                        location.x,
                        location.y);

                e.addComponent(new Update());
                e.addToWorld();

                world.getManager(GroupManager.class).add(e, getGroupName());
            }
        }
        lastSpawn = System.currentTimeMillis();
    }

}
