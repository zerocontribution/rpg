package io.zerocontribution.winter.network;

import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;

public interface Message {
    void receive(Connection pc);
}
