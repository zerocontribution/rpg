package io.zerocontribution.winter.components;

import com.artemis.Component;

public class GridPosition extends Component {
    public float x, y;

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
