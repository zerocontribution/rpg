package io.zerocontribution.winter.ai.normals;

import com.artemis.World;
import io.zerocontribution.winter.ai.AI;
import io.zerocontribution.winter.ai.modules.AIModule;
import io.zerocontribution.winter.ai.modules.BasicFollow;

/**
 * The dumbest AI. Follow a player around.
 */
public class ZombieAI extends AI {

    public ZombieAI(World world) {
        super(world);

        modules = new AIModule[1];
        modules[0] = new BasicFollow(world, 100, 100);
    }
}
