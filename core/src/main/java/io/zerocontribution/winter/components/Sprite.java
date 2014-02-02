package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

public class Sprite extends Component {
    public Texture texture;

    public Sprite(String texturePath) {
        this.texture = new Texture(texturePath);
    }

}
