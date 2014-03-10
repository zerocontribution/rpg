package io.zerocontribution.winter.components;

public class Velocity extends EntityComponent {

    public float x;
    public float y;

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
        setUpdated();
    }

    public void setX(float x) {
        this.x = x;
        setUpdated();
    }

    public void setY(float y) {
        this.y = y;
        setUpdated();
    }

    public String toString() {
        return "Velocity[" + String.valueOf(x) + "," + String.valueOf(y) + "]";
    }

}
