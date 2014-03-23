package io.zerocontribution.winter.components;

public class Velocity extends EntityComponent {

    public float x, y;

    public void set(float x, float y) {
        if (this.x == x && this.y == y) {
            return;
        }

        this.x = x;
        this.y = y;
        setUpdated();
    }

    public void setX(float x) {
        if (this.x == x) {
            return;
        }

        this.x = x;
        setUpdated();
    }

    public void setY(float y) {
        if (this.y == y) {
            return;
        }

        this.y = y;
        setUpdated();
    }

    public String toString() {
        return "Velocity[" + String.valueOf(x) + "," + String.valueOf(y) + "]";
    }

}
