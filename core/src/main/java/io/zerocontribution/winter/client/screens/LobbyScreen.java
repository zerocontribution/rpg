package io.zerocontribution.winter.client.screens;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import io.zerocontribution.winter.DeprecatedAssets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.components.Player;
import io.zerocontribution.winter.ui.groups.CharacterSelection;
import io.zerocontribution.winter.ui.groups.PlayerListing;
import io.zerocontribution.winter.ui.hud.Chat;

public class LobbyScreen extends AbstractScreen {

    Table rootTable = new Table(skin);
    Chat chat;
    Button startButton;
    int acc = 0;

    ImmutableBag<Entity> knownPlayers = WinterGame.world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS);

    public LobbyScreen(WinterGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        checkPlayerStates();
        super.render(delta);
    }

    private void checkPlayerStates() {
        if (acc++ > 10) {
            if (WinterGame.getInstance().isHost()) {
                boolean startDisabled = false;
                for (int i = 0; i < knownPlayers.size(); i++) {
                    Player player = knownPlayers.get(i).getComponent(Player.class);
                    if (!player.hasGameClass()) {
                        startDisabled = true;
                    }
                }
                startButton.setDisabled(startDisabled);
            }

            acc = 0;
        }
    }

    public void show() {
        super.show();

        stage.addActor(rootTable);
        rootTable.setFillParent(true);
        rootTable.top();
        rootTable.debug();

        String title = WinterGame.getInstance().isHost() ? "Create Multiplayer Game" : "Join Multiplayer Game";
        rootTable.add(title).pad(20).left();
        rootTable.row();

        CharacterSelection characterSelection = createCharacterSelection();
        rootTable.add(characterSelection).padTop(50);
        rootTable.row();

        Table playerList = createPlayerList();
        rootTable.add(playerList).top().left().expand();

        Table chat = createChat();
        rootTable.row();
        rootTable.add(chat).bottom().left().expandX();

        if (WinterGame.getInstance().isHost()) {
            Table controls = createHostControls();
            rootTable.row();
            rootTable.add(controls).bottom().align(Align.right).expandX();
        }

    }

    private Table createPlayerList() {
        Table playerList = new Table(skin);
        playerList.setName("playerList");
        playerList.debug();
        playerList.bottom().left();
        playerList.defaults().align(Align.left).minHeight(30);

        playerList.add("Players");

        PlayerListing listing;
        for (int i = 0; i < knownPlayers.size(); i++) {
            listing = new PlayerListing(knownPlayers.get(i).getComponent(Player.class));
            playerList.row();
            playerList.add(listing);
        }

        return playerList;
    }

    private CharacterSelection createCharacterSelection() {
        return new CharacterSelection(DeprecatedAssets.classes.classes);
    }

    private Table createHostControls() {
        Table controls = new Table(skin);
        controls.align(Align.right);
        controls.setPosition(Gdx.graphics.getWidth() - 20, 20);
        controls.defaults().align(Align.right).width(150).height(50);

        if (WinterGame.getInstance().isHost()) {
            startButton = new TextButton("Start!", skin);
            startButton.setDisabled(true);
            startButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (!startButton.isDisabled()) {
                        WinterGame.gameClient.sendStartGame();
                    }
                    return true;
                }
            });
            controls.add(startButton);
        }

        return controls;
    }

    private Table createChat() {
        Table chat = new Table(skin);
        chat.debug();
        chat.align(Align.left);
        chat.setPosition(20, 20);
        chat.setWidth(Chat.PREF_WIDTH);
        chat.defaults().align(Align.left).width(Chat.PREF_WIDTH).height(200);

        this.chat = new Chat();
        chat.add(this.chat);

        // TODO Chat
        return chat;
    }

    @Override
    public void dispose() {
        super.dispose();
        chat.dispose();
    }

}
