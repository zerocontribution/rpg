package io.zerocontribution.winter.components;

import com.artemis.Component;
import io.zerocontribution.winter.ai.AI;

public class Npc extends EntityComponent {
    public AI processor;

    public Npc() {}
    public Npc(AI processor) {
        this.processor = processor;
    }
}
