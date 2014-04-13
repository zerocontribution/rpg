package io.zerocontribution.winter.network;

import com.esotericsoftware.kryonet.Connection;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.event.GameEvent;
import io.zerocontribution.winter.ui.hud.Chat;

public class ChatMessage extends EventMessage {
    public String name;
    public String message;

    public ChatMessage() {}
    public ChatMessage(String message) {
        this.message = message;
    }

    public void receive(Connection pc) {
        WinterGame.events.notify(new ChatMessageEvent(this));
    }

    public static class ChatMessageEvent implements GameEvent<Chat> {
        private ChatMessage message;

        public ChatMessageEvent(ChatMessage message) {
            this.message = message;
        }

        public void notify(Chat listener) {
            listener.displayMessage(message.name, message.message);
        }
    }

}
