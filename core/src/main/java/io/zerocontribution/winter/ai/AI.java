package io.zerocontribution.winter.ai;

import com.artemis.Entity;
import com.artemis.World;
import io.zerocontribution.winter.ai.modules.AIModule;

/**
 * @todo Would like to make it so World doesn't have to be passed along the chain to get to AI modules.
 *
 * @link https://github.com/aimarrod/Sons-of-Cydonia
 */
public abstract class AI {

    public AIModule[] modules;

    private final World world;

    private boolean initialized = false;

    public AI(World world) {
        this.world = world;
    }

    public void initialize() {
        if (!initialized) {
            for (int i = 0; i < modules.length; i++) {
                modules[i].initialize();
            }
            initialized = true;
        }
    }

    public void process(Entity e) {
        processModules(e);
    }

    protected void processModules(Entity e) {
        for (int i = 0; i < modules.length; i++) {
            if (modules[i].process(e)) break;
        }
    }

}
