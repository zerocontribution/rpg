package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;

public class JoinCreateGameScreen extends AbstractScreen {

    public JoinCreateGameScreen() {
        super(WinterGame.getInstance());
    }

    @Override
    public void show() {
        super.show();

        final TextField playerName = new TextField(Constants.UI.DEFAULT_PLAYER_NAME, skin);

        TextButton createButton = new TextButton("Host Game", skin);
        createButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.startServer();
                WinterGame.gameClient.connect("localhost");
                WinterGame.gameClient.localName = playerName.getText();
                game.setScreen(new LobbyScreen(game));
                return true;
            }
        });

        final TextField hostIPField = new TextField("Host IP Address", skin);
        TextButton joinButton = new TextButton("Join", skin);
        joinButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WinterGame.gameClient.connect(hostIPField.getText());
                WinterGame.gameClient.localName = playerName.getText();
                game.setScreen(new LobbyScreen(game));
                return true;
            }
        });

        TextButton exitButton = new TextButton("Main Menu", skin);
        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WinterGame.getInstance().stopServer();
                WinterGame.getInstance().setScreen(new MenuScreen());
                return true;
            }
        });

        BitmapFont font = new BitmapFont(
                Gdx.files.internal("assets/fonts/normal.fnt"),
                Gdx.files.internal("assets/fonts/normal_0.png"),
                false);

        Table table = new Table();
        table.setFillParent(true);
        table.add(playerName).width(400).height(50);
        table.row();
        table.add(new Label("Host a Game", skin)).height(50);
        table.row();
        table.add(createButton).width(400).height(50);
        table.row();
        table.add(new Label("Join a Game", skin)).height(50);
        table.row();
        table.add(hostIPField).width(400).height(50);
        table.row();
        table.add(joinButton).width(400).height(50);
        table.row();
        table.add(new Label("", skin)).height(50);
        table.row();
        table.add(exitButton).width(400).height(50);

        stage.addActor(table);
    }
}
