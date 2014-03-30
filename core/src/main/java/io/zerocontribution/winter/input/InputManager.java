package io.zerocontribution.winter.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

public class InputManager {

    private static InputMultiplexer inputProcessor;

    static {
        inputProcessor = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public static InputMultiplexer getInputProcessor() {
        return inputProcessor;
    }

}
