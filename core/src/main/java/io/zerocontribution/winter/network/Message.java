package io.zerocontribution.winter.network;

import com.esotericsoftware.kryonet.Connection;

public interface Message {
    void receive(Connection pc);
}
