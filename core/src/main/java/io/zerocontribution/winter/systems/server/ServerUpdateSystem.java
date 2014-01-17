package io.zerocontribution.winter.systems.server;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import io.zerocontribution.winter.components.BaseComponent;
import io.zerocontribution.winter.components.Update;
import io.zerocontribution.winter.utils.ServerGlobals;

/**
 * @todo IntervalEntitySystem?
 */
public class ServerUpdateSystem extends EntitySystem {

    private long interval;
    private long acc;

    @SuppressWarnings("unchecked")
    public ServerUpdateSystem(long interval) {
        super(Aspect.getAspectForAll(Update.class));
        this.interval = interval;
    }

    @Override
    protected boolean checkProcessing() {
        if (ServerGlobals.time >= acc) {
            acc = ServerGlobals.time + interval;
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        Bag bag = new Bag(100);

        Bag<Component> components = new Bag<Component>(20);
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            components.clear();
            entity.getComponents(components);

            for (int j = 0; j < components.size(); j++) {
                BaseComponent baseComponent = (BaseComponent) components.get(i);
                if (baseComponent.isUpdated()) {
                    Object transferable = baseComponent.create(entity);
                    if (transferable != null) {
                        bag.add(transferable);
                    }
                }
            }
        }

        if (bag.size() > 0) {
            ServerGlobals.updates = bag;
        }
    }

}
