package io.zerocontribution.winter.ai;

import com.artemis.Entity;
import io.zerocontribution.winter.ai.modules.AIModule;

/**
 * @link https://github.com/aimarrod/Sons-of-Cydonia
 */
public abstract class AI {

    public AIModule[] modules;

    public void process(Entity e) {
        processModules(e);
    }

    protected void processModules(Entity e) {
        for (int i = 0; i < modules.length; i++) {
            if (modules[i].process(e)) break;
        }
    }

}
