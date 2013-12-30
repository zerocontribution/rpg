package io.zerocontribution.winter.components;

import com.artemis.Component;
import io.zerocontribution.winter.Directions;

public class Facing extends Component {
    public Directions direction;

    public Facing(Directions direction) {
        this.direction = direction;
    }

}
