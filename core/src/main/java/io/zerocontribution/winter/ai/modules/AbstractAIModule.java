package io.zerocontribution.winter.ai.modules;

import com.artemis.World;

public abstract class AbstractAIModule implements AIModule {

    public World world;

    public AbstractAIModule(World world) {
        this.world = world;
    }

    public void initialize() {}

}
