package io.zerocontribution.winter.server;

import com.artemis.Entity;
import com.artemis.World;
import io.zerocontribution.winter.AbstractEntityFactory;

public class ServerEntityFactory extends AbstractEntityFactory {

    @Override
    public Entity createMap(World world) {
        return null;
    }

    @Override
    public Entity createBlockingTile(World world, float x, float y) {
        return null;
    }
}
