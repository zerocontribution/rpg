package io.zerocontribution.winter.components;

import com.artemis.Component;

public class Position extends EntityComponent {
    public float x, y;

    public Position() {}
    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String toLog() {
        return new StringBuilder()
                .append("Position[")
                .append(x).append(",").append(y)
                .append("]")
                .toString();
    }
}
