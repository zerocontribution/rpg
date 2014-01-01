package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import io.zerocontribution.winter.components.Npc;

public class AIProcessingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Npc> npcMapper;

    public AIProcessingSystem() {
        super(Aspect.getAspectForAll(Npc.class));
    }

    @Override
    protected void process(Entity e) {
        npcMapper.get(e).processor.initialize();
        npcMapper.get(e).processor.process(e);
    }

}
