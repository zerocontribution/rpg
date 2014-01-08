package io.zerocontribution.winter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.zerocontribution.winter.screens.IsoTiledMapScreen;
import io.zerocontribution.winter.screens.MenuScreen;

public class WinterGame extends Game {

    @Override
    public void create () {
        setScreen(new MenuScreen(this));
//        setScreen(new IsoTiledMapScreen());
    }

    @Override
    public void render () {
        super.render();
        // TODO Add world reset
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            try {
                setScreen(getScreen().getClass().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
