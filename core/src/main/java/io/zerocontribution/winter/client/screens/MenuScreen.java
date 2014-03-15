package io.zerocontribution.winter.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.zerocontribution.winter.WinterGame;

public class MenuScreen extends AbstractScreen {

    public MenuScreen() {
        super(WinterGame.getInstance());
    }

    @Override
    public void show() {
        super.show();

        TextButton playSingleButton = new TextButton("Play Singleplayer", skin);
        TextButton playMultiButton = new TextButton("Play Multiplayer", skin);

        TextButton optionsButton = new TextButton("Options", skin); // TODO set player name, etc here?
        TextButton exitButton = new TextButton("Exit", skin);

        playSingleButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.startServer();
                WinterGame.gameClient.connect("localhost");
                WinterGame.gameClient.sendStartGame();
                game.setScreen(new LobbyScreen(game));
                return true;
            }
        });
        playMultiButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new JoinCreateGameScreen());
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
        table.add(playSingleButton).width(300).height(50);
        table.row();
        table.add(playMultiButton).width(300).height(50);
        table.row();
        table.add(optionsButton).width(300).height(50);
        table.row();
        table.add(exitButton).width(300).height(50);

        stage.addActor(table);
    }

}
