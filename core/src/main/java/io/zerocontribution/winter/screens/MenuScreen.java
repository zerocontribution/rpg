package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.WinterGame;

public class MenuScreen extends AbstractScreen {

    WinterGame game;

    Stage stage;

    // TODO Would be better to have a GameWrapper class to use for IOC, etc.
    public MenuScreen(WinterGame game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = createSkin();

        Table table = new Table();

        TextButton newGameButton = new TextButton("New Game", skin);
        TextButton newNetworkGameButton = new TextButton("New Online Game", skin);
        TextButton joinNetworkGameButton = new TextButton("Join Online Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new IsoTiledMapScreen());
                return true;
            }
        });

        table.setFillParent(true);
        table.add(newGameButton).width(150).height(50);
        table.row();
        table.add(newNetworkGameButton).width(150).height(50);
        table.row();
        table.add(joinNetworkGameButton).width(150).height(50);
        table.row();
        table.add(exitButton).width(150).height(50);

        stage.addActor(table);
    }

    private Skin createSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        skin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        return skin;
    }

}
