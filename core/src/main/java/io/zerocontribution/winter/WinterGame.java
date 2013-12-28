package io.zerocontribution.winter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.zerocontribution.winter.screens.TiledMapScreen;

public class WinterGame extends Game {

    @Override
    public void create () {
        setScreen(new TiledMapScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render () {
        super.render();
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

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

}
