package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.artemis.Entity;

public class Damage extends Component {
    public Entity ability;
    public Entity source;
    public int healthAmount;
}