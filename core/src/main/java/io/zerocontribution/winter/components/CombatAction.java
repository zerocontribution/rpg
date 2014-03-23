package io.zerocontribution.winter.components;

import com.artemis.Entity;
import io.zerocontribution.winter.combat.processors.CombatProcessor;

public class CombatAction extends BaseComponent {

    public Entity ability;
    public CombatProcessor processor;
    public Entity target;

    public CombatAction(Entity ability, CombatProcessor processor, Entity target) {
        this.ability = ability;
        this.processor = processor;
        this.target = target;
    }

}
