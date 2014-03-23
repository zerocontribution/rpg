package io.zerocontribution.winter.spawners;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.components.Update;
import io.zerocontribution.winter.server.ServerEntityFactory;

import java.util.List;

public class IntervalSpawner extends Spawner {

    public List<String> enemies;
    public int increment;
    public float interval;
    public String zone;
    public long lastSpawn = System.currentTimeMillis();

    private Rectangle boundary;

    private boolean shouldSpawn() {
        return (System.currentTimeMillis() - lastSpawn) / 1000 >= interval;
    }

    public void spawn(World world) {
        if (shouldSpawn()) {
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
                }
            }
            lastSpawn = System.currentTimeMillis();
        }
    }

}
