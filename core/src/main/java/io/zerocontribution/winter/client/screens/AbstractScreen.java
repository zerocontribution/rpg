package io.zerocontribution.winter.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import io.zerocontribution.winter.WinterGame;

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
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
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
        skin.dispose();
        stage.dispose();
    }

    protected void createSkin() {
        skin = new Skin(Gdx.files.internal("assets/ui/HoloSkin/Holo-dark-hdpi.json"));
//        skin = new Skin();
//
//        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.WHITE);
//        pixmap.fill();
//        skin.add("white", new Texture(pixmap));
//
//        BitmapFont font = new BitmapFont(
//                Gdx.files.internal("assets/fonts/normal.fnt"),
//                Gdx.files.internal("assets/fonts/normal_0.png"),
//                false);
//        skin.add("default", font);
//
//        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
//        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
//        textButtonStyle.checked = skin.newDrawable("white", Color.CYAN);
//        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
//        textButtonStyle.font = skin.getFont("default");
//        skin.add("default", textButtonStyle);
//
//        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
//        textFieldStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
//        textFieldStyle.focusedBackground = skin.newDrawable("white", Color.LIGHT_GRAY);
//        textFieldStyle.disabledBackground = skin.newDrawable("white", Color.DARK_GRAY);
//        textFieldStyle.cursor = skin.newDrawable("white", Color.CYAN);
//        textFieldStyle.font = skin.getFont("default");
//        textFieldStyle.fontColor = Color.CYAN;
//        textFieldStyle.disabledFontColor = Color.LIGHT_GRAY;
//        textFieldStyle.messageFont = skin.getFont("default");
//        textFieldStyle.messageFontColor = Color.CYAN;
//        textFieldStyle.selection = skin.newDrawable("white", Color.CYAN);
//        skin.add("default", textFieldStyle);
//
//        Label.LabelStyle labelStyle = new Label.LabelStyle();
//        labelStyle.background = skin.newDrawable("white", Color.BLACK);
//        labelStyle.font = skin.getFont("default");
//        labelStyle.fontColor = Color.WHITE;
//        skin.add("default", labelStyle);
    }

}
