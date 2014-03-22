package io.zerocontribution.winter.systems.server;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.components.BaseComponent;
import io.zerocontribution.winter.components.Update;
import io.zerocontribution.winter.network.Network;
import io.zerocontribution.winter.utils.ServerGlobals;

import java.util.ArrayList;

public class ServerUpdateSystem extends IntervalEntitySystem {

    @SuppressWarnings("unchecked")
    public ServerUpdateSystem(float interval) {
        super(Aspect.getAspectForAll(Update.class), interval);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        ArrayList<Network.EntityData> bag = new ArrayList<Network.EntityData>(20);

        Bag<Component> components = new Bag<Component>(10);
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            components.clear();
            components = entity.getComponents(components);

            for (int j = 0; j < components.size(); j++) {
                BaseComponent baseComponent = (BaseComponent) components.get(j);
                if (baseComponent.isUpdated()) {
                    Object transferable = baseComponent.create(entity);
                    if (transferable != null) {
                        bag.add((Network.EntityData) transferable);
                    }
                    baseComponent.setUpdated(false);
                }
            }

            entity.removeComponent(Update.class);
            entity.changedInWorld();

            // Known limitation: Prevent buffer sizes from getting out of hand on large updates. Splits updates across
            // multiple cycles.
            // TODO: This may be better done in the ServerNetworkSystem and just burst multiple packets in a single tick.
            if (bag.size() > 50) {
                Log.warn("ServerUpdateSystem", "Updates incomplete: Reached component limit");
                break;
            }
        }

        if (bag.size() > 0) {
            ServerGlobals.updates = bag;
        }
    }

}
