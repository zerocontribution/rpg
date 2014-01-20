package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.zerocontribution.winter.WinterGame;

public class MenuScreen extends AbstractScreen {

    public MenuScreen(WinterGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        TextButton createGameButton = new TextButton("New Game", skin);
        TextButton joinGameButton = new TextButton("Join Game", skin);
        TextButton optionsButton = new TextButton("Options", skin); // TODO set player name, etc here.
        TextButton exitButton = new TextButton("Exit", skin);

        createGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.startServer();
                WinterGame.gameClient.connect("localhost");
                game.setScreen(new LobbyScreen(game));
                return true;
            }
        });
        joinGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new JoinNetworkGameScreen(game));
                return true;
            }
        });
        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(createGameButton).width(150).height(50);
        table.row();
        table.add(joinGameButton).width(150).height(50);
        table.row();
        table.add(optionsButton).width(150).height(50);
        table.row();
        table.add(exitButton).width(150).height(50);

        stage.addActor(table);
    }

}
