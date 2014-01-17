package io.zerocontribution.winter.components;

import com.artemis.Entity;
import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.network.Network;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;

public class EntityComponent extends BaseComponent {

    @Override
    public Network.EntityData create(Entity entity) {
        return new Network.EntityData(entity, this);
    }

    public void receive(Connection pc, Entity entity) {
        entity.addComponent(this);
    }

}
