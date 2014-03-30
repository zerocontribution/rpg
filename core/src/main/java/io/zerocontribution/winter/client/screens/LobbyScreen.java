package io.zerocontribution.winter.client.screens;

import com.artemis.ComponentManager;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.components.Player;
import io.zerocontribution.winter.ui.UIManager;

/**
 * @todo Add chat
 */
public class LobbyScreen extends AbstractScreen {

    public LobbyScreen(WinterGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        stage.clear();

        Table players = buildPlayersList();

        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stack.add(players);

//        Table layout = new Table(skin);
//        layout.align(Align.top).padBottom(20).padTop(20);
//        layout.defaults().space(10);
//
//        layout.setWidth(Gdx.graphics.getWidth());
//        layout.setHeight(Gdx.graphics.getHeight());
//        layout.setX(-Gdx.graphics.getWidth() / 2);
//        layout.setY(0);`
//
//        layout.add("Multiplayer Lobby").left().top();
//        layout.row();
//        layout.add(players).left();
//        layout.row();
//
//        stage.addActor(layout);

        super.render(delta);
    }

    private Table buildPlayersList() {
        Table players = new Table(skin);
        players.debug();
        players.setFillParent(true);
        players.setX(0);

        ImmutableBag<Entity> knownPlayers = WinterGame.world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS);
        ComponentMapper<Player> playerMapper = WinterGame.world.getMapper(Player.class);
        for (int i = 0; i < knownPlayers.size(); i++) {
            players.add(playerMapper.get(knownPlayers.get(i)).name);
            players.row();
        }

        return players;
    }

    /**
     * @todo Add map selection & game rules
     * @todo Change to a builder
     */
    protected void showHostUIControls() {
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
