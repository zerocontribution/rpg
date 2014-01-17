package io.zerocontribution.winter.components;

import com.artemis.Component;

public class Dimensions extends EntityComponent {
    public float width, height;

    public Dimensions() {}
    public Dimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }

}
