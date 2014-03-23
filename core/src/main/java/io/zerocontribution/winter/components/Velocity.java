package io.zerocontribution.winter.components;

public class Velocity extends EntityComponent {

    public float x, y;
    transient public float lastX, lastY;

    public void set(float x, float y) {
        if (this.x == x && this.y == y) {
            return;
        }
        lastX = this.x;
        lastY = this.y;

        this.x = x;
        this.y = y;
        setUpdated();
    }

    public void setX(float x) {
        if (this.x == x) {
            return;
        }
        lastX = this.x;

        this.x = x;
        setUpdated();
    }

    public void setY(float y) {
        if (this.y == y) {
            return;
        }
        lastY = this.y;

        this.y = y;
        setUpdated();
    }

    public boolean changed() {
        return lastY != y || lastX != x;
    }

    public String toString() {
        return "Velocity[" + String.valueOf(x) + "," + String.valueOf(y) + "]";
    }

}
