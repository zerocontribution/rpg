package io.zerocontribution.winter.components;

public class SpriteColor extends EntityComponent {

    public float r = 1;
    public float g = 1;
    public float b = 1;
    public float a = 1;

    public SpriteColor() {}
    public SpriteColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

}
