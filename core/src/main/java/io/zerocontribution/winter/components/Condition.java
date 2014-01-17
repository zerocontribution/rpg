package io.zerocontribution.winter.components;

import com.artemis.Component;
import io.zerocontribution.winter.State;

public class Condition extends EntityComponent {
    public State state;

    public Condition() {}
    public Condition(State state) {
        this.state = state;
    }
}
