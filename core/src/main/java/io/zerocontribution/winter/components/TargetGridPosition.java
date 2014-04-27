package io.zerocontribution.winter.components;

import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.ai.pathfinding.Path;
import io.zerocontribution.winter.ai.pathfinding.PathFinder;

public class TargetGridPosition extends BaseComponent {
    public float x, y;
    public PathFinder pathFinder;
    public Path path;
    public int currentPathStep = 1;

    public TargetGridPosition() {
        this.x = -1;
        this.y = -1;
    }
    public TargetGridPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 target) {
        x = target.x;
        y = target.y;
        path = null;
        currentPathStep = 1;
    }

    public boolean hasTarget() {
        return x != -1 && y != -1;
    }

    public boolean at(GridPosition gridPosition) {
        return gridPosition.x == x && gridPosition.y == y;
    }

    public void reset() {
        x = -1;
        y = -1;
        path = null;
        currentPathStep = 1;
    }

    public String toLog() {
        return new StringBuilder()
                .append("TargetGridPosition[")
                .append(x).append(",").append(y)
                .append("]")
                .toString();
    }

}
