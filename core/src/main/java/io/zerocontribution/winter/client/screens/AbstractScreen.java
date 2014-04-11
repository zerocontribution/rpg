package io.zerocontribution.winter.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.input.InputManager;

public abstract class AbstractScreen implements Screen {

    WinterGame game;

    Stage stage;

    Skin skin;

    public AbstractScreen(WinterGame game) {
        this.game = game;
        createSkin();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        Table.drawDebug(stage);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage();
        InputManager.getInputProcessor().addProcessor(stage);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        InputManager.getInputProcessor().removeProcessor(stage);
        stage.dispose();
    }

    protected void createSkin() {
        skin = new Skin(Gdx.files.internal("assets/ui/HoloSkin/Holo-dark-hdpi.json"));
    }

}
