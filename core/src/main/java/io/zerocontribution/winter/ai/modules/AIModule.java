package io.zerocontribution.winter.ai.modules;

import com.artemis.Entity;

// TODO Need to support aspects
public interface AIModule {
    public void initialize();
    public boolean process(Entity e);
}
