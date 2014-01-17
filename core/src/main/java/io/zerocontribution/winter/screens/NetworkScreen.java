package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import io.zerocontribution.winter.WinterGame;

public class NetworkScreen extends AbstractScreen {

    private Stage stage;

    public NetworkScreen(WinterGame game) {
        super(game);
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

        final TextField nameTextField = new TextField("Player Name", skin);
        TextButton createGameButton = new TextButton("Create Game", skin);
        TextButton joinGameButton = new TextButton("Join Game", skin);

        createGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.startServer();
                game.setScreen(new IsoTiledMapScreen(game, true, nameTextField.getText()));
                return true;
            }
        });
        joinGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new IsoTiledMapScreen(game, false, nameTextField.getText()));
                return true;
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(nameTextField).width(400).height(30);
        table.row();
        table.add(createGameButton).width(400).height(50);
        table.row();
        table.add(joinGameButton).width(400).height(50);

        stage.addActor(table);
    }
}
