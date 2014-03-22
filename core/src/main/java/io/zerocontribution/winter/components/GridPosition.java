package io.zerocontribution.winter.components;

public class GridPosition extends EntityComponent {
    public float x, y;

    public GridPosition() {}
    public GridPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String toLog() {
        return new StringBuilder()
                .append("GridPosition[")
                .append(x).append(",").append(y)
                .append("]")
                .toString();
    }
}
