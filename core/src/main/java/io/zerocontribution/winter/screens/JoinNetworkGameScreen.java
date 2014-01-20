package io.zerocontribution.winter.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import io.zerocontribution.winter.WinterGame;

public class JoinNetworkGameScreen extends AbstractScreen {

    public JoinNetworkGameScreen(WinterGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        final TextField hostIPField = new TextField("Host IP Address", skin);
        TextButton joinButton = new TextButton("Join", skin);

        joinButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WinterGame.gameClient.connect(hostIPField.getText());
                game.setScreen(new LobbyScreen(game));
                return true;
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(hostIPField).width(150).height(50);
        table.row();
        table.add(joinButton).width(150).height(50);

        stage.addActor(table);
    }

}
