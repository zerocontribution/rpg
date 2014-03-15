package io.zerocontribution.winter.ai.modules;

import com.artemis.Entity;

public interface AIModule {
    public void initialize();
    public boolean process(Entity e);
}
