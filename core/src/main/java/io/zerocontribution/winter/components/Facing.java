package io.zerocontribution.winter.components;

import io.zerocontribution.winter.struct.Directions;

public class Facing extends EntityComponent {
    public Directions direction;

    public Facing() {}
    public Facing(Directions direction) {
        this.direction = direction;
    }

}
