package io.zerocontribution.winter.components;

public class Position extends EntityComponent {
    public float x, y;

    public Position() {}
    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
        setUpdated();
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
        setUpdated();
    }

    public String toLog() {
        return new StringBuilder()
                .append("Position[")
                .append(x).append(",").append(y)
                .append("]")
                .toString();
    }
}
