package io.zerocontribution.winter.components;

import io.zerocontribution.winter.ai.AI;

public class Npc extends EntityComponent {
    transient public AI ai;

    public Npc() {}
    public Npc(AI ai) {
        this.ai = ai;
    }

}
