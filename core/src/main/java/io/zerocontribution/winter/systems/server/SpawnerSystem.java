package io.zerocontribution.winter.systems.server;

import com.artemis.systems.VoidEntitySystem;
import io.zerocontribution.winter.spawners.Spawner;
import io.zerocontribution.winter.utils.ServerGlobals;

public class SpawnerSystem extends VoidEntitySystem {

    @Override
    protected boolean checkProcessing() {
        return ServerGlobals.currentMapAsset != null;
    }

    protected void processSystem() {
        for (Spawner spawner : ServerGlobals.currentMapAsset.spawners) {
            spawner.spawn(world);
        }
    }

}
