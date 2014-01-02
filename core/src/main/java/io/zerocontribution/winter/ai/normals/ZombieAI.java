package io.zerocontribution.winter.ai.normals;

import com.artemis.World;
import io.zerocontribution.winter.ai.AI;
import io.zerocontribution.winter.ai.modules.AIModule;
import io.zerocontribution.winter.ai.modules.BasicAttack;
import io.zerocontribution.winter.ai.modules.BasicFollow;
import io.zerocontribution.winter.ai.modules.BasicVision;

/**
 * The dumbest AI. Follow a player around.
 */
public class ZombieAI extends AI {

    public ZombieAI(World world) {
        super(world);

        modules = new AIModule[3];
        modules[0] = new BasicVision(world, 250, 500);
        modules[1] = new BasicFollow(world, 100, 100);
        modules[2] = new BasicAttack(world, 50, 2);
    }
}
