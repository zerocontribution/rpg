package io.zerocontribution.winter.components;

import com.artemis.Component;

public class Velocity extends EntityComponent {

    public float x, y;

    public String toString() {
        return "Velocity[" + String.valueOf(x) + "," + String.valueOf(y) + "]";
    }

}
