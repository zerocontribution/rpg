package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationSprite extends Component {

    public enum Layer {
        DEFAULT,
        BACKGROUND,
        ACTORS_1,
        ACTORS_2,
        ACTORS_3,
        PARTICLES;

        public int getLayerId() {
            return ordinal();
        }
    }

    public String name;
    public Animation animation;
    public int width;
    public int height;
    public int base;
    public int rows;
    public int cols;
    public float frameTime;
    public boolean active = true;
    public boolean loop = true;
    public float time = 0;
    public float scaleX = 1;
    public float scaleY = 1;
    public float rotation;
    public float r = 1;
    public float g = 1;
    public float b = 1;
    public float a = 1;
    public Layer layer = Layer.DEFAULT;

}
