package io.zerocontribution.winter.network;

import com.esotericsoftware.kryonet.Connection;

public abstract class EventMessage implements Message {

    @Override
    public void receive(Connection pc) {}

}
