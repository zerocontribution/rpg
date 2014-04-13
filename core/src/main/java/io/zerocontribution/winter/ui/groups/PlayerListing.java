package io.zerocontribution.winter.ui.groups;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.zerocontribution.winter.components.Player;
import io.zerocontribution.winter.ui.UIManager;

public class PlayerListing extends Group {

    Label readyState;

    public PlayerListing(final Player player) {
        Skin skin = UIManager.getInstance().getSkin();
        Label playerName = new Label(player.name + ":", skin);
        playerName.setFontScale(0.8f);
        addActor(playerName);

        readyState = new Label(player.hasGameClass() ? "Ready (" + player.getGameClass() + ")" : "Not Ready", skin);
        readyState.setPosition(getX() + playerName.getWidth(), getY());
        readyState.setFontScale(0.8f);
        addActor(readyState);
    }

    public void updateReadyState() {
        // TODO Should this be an event listener?
    }

}
