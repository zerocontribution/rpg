package io.zerocontribution.winter.network;

import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;

public abstract class EventMessage implements Message {

    @Override
    public void receive(Connection pc) {}

}
