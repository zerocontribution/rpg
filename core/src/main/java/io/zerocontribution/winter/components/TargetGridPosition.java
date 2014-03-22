package io.zerocontribution.winter.components;

import io.zerocontribution.winter.ai.pathfinding.Path;
import io.zerocontribution.winter.ai.pathfinding.PathFinder;

public class TargetGridPosition extends BaseComponent {
    public float x, y;
    public PathFinder pathFinder;
    public Path path;
    public int currentPathStep = 1;

    public TargetGridPosition() {}
    public TargetGridPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String toLog() {
        return new StringBuilder()
                .append("TargetGridPosition[")
                .append(x).append(",").append(y)
                .append("]")
                .toString();
    }

}
