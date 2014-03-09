package io.zerocontribution.winter.components;

import com.artemis.Component;

public class AnimationTimer extends EntityComponent {
    public float time;

    public AnimationTimer() {}
    public AnimationTimer(float time) {
        this.time = time;
    }

}
