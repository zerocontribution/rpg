package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.zerocontribution.winter.WinterGame;

public class MenuScreen extends AbstractScreen {

    Stage stage;

    public MenuScreen(WinterGame game) {
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

        Table table = new Table();

        TextButton newGameButton = new TextButton("New Game", skin);
//        TextButton newNetworkGameButton = new TextButton("New Online Game", skin);
//        TextButton joinNetworkGameButton = new TextButton("Join Online Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new NetworkScreen(game));
                return true;
            }
        });

        table.setFillParent(true);
        table.add(newGameButton).width(150).height(50);
        table.row();
//        table.add(newNetworkGameButton).width(150).height(50);
//        table.row();
//        table.add(joinNetworkGameButton).width(150).height(50);
//        table.row();
        table.add(exitButton).width(150).height(50);

        stage.addActor(table);
    }

}
