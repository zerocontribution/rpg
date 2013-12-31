package io.zerocontribution.winter.components;

import com.artemis.Component;
import io.zerocontribution.winter.ai.AI;

public class Npc extends Component {
    public AI processor;

    public Npc(AI processor) {
        this.processor = processor;
    }
}
