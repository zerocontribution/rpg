package io.zerocontribution.winter.systems.client;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import io.zerocontribution.winter.components.Damage;

/**
 * Pretty simple: Just cleaning up components. I don't think that there will need to be anything in here.
 */
public class ClientDamageProcessingSystem extends EntityProcessingSystem {

    @SuppressWarnings("unchecked")
    public ClientDamageProcessingSystem() {
        super(Aspect.getAspectForAll(Damage.class));
    }

    @Override
    protected void process(Entity e) {
        e.removeComponent(Damage.class);
        e.changedInWorld();
    }

}
