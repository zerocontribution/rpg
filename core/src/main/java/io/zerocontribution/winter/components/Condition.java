package io.zerocontribution.winter.components;

import com.artemis.Component;
import io.zerocontribution.winter.State;

public class Condition extends Component {
    public State state;

    public Condition(State state) {
        this.state = state;
    }
}
