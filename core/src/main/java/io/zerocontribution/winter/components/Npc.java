package io.zerocontribution.winter.components;

import com.artemis.Component;
import io.zerocontribution.winter.ai.AI;

// TODO AI processor should just be a name: I shouldn't need clients to know how
// the server processes the AI, nor send the data across the wire.
public class Npc extends EntityComponent {
    public String aiName;

    private AI ai;

    public Npc() {}
    public Npc(AI ai) {
        this.ai = ai;
        this.aiName = ai.name;
    }

    public AI getAI() {
        return ai;
    }

}
