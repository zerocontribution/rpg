package io.zerocontribution.winter.systems.client;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.components.Damage;
import io.zerocontribution.winter.ui.hud.ScrollingCombatText;

/**
 * Pretty simple: Just cleaning up components. I don't think that there will need to be anything in here.
 */
public class ClientDamageProcessingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Damage> damageMapper;

    @SuppressWarnings("unchecked")
    public ClientDamageProcessingSystem() {
        super(Aspect.getAspectForAll(Damage.class));
    }

    @Override
    protected void process(Entity e) {
        Damage damage = damageMapper.get(e);

        WinterGame.events.notify(new ScrollingCombatText.ScrollingCombatTextEvent(String.valueOf(damage.healthAmount)));

        e.removeComponent(Damage.class);
        e.changedInWorld();
    }

}
