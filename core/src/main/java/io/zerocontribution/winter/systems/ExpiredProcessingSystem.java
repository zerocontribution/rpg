package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import io.zerocontribution.winter.components.Expiring;

public class ExpiredProcessingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Expiring> expiringMapper;

    @SuppressWarnings("unchecked")
    public ExpiredProcessingSystem() {
        super(Aspect.getAspectForAll(Expiring.class));
    }

    @Override
    protected void process(Entity e) {
        Expiring expiring = expiringMapper.get(e);

        expiring.expiration -= world.delta;
        if (expiring.expiration <= 0) {
            e.deleteFromWorld();
        }
    }
}
