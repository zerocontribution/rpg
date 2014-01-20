package io.zerocontribution.winter.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.utils.ClientGlobals;

/**
 * @todo Add chat
 */
public class LobbyScreen extends AbstractScreen {

    public LobbyScreen(WinterGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        if (game.isHost()) {
            showHostUI();
        }

        // TODO Character selection

        if (false) {
            TextButton newCharacterButton = new TextButton("New Character", skin);

            Table table = new Table();
            table.setX(0);
            table.setFillParent(true);
            table.add(newCharacterButton).width(150).height(50);

            stage.addActor(table);
        }
    }

    /**
     * @todo Add map selection & game rules
     */
    protected void showHostUI() {
        TextButton startButton = new TextButton("Start!", skin);

        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WinterGame.gameClient.sendStartGame();
                return true;
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(startButton).width(150).height(50);
        table.row();

        stage.addActor(table);
    }

}
