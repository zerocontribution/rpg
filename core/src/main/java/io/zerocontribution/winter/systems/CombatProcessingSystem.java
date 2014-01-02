package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import io.zerocontribution.winter.combat.processors.CombatProcessor;
import io.zerocontribution.winter.components.CombatAction;

public class CombatProcessingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<CombatAction> combatActionMapper;

    @SuppressWarnings("unchecked")
    public CombatProcessingSystem() {
        super(Aspect.getAspectForAll(CombatAction.class));
    }

    @Override
    protected void process(Entity e) {
        CombatProcessor processor = combatActionMapper.get(e).processor;
        processor.initialize(world);
        processor.process(e);
    }

}
