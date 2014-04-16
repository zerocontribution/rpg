package io.zerocontribution.winter.ui.groups;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.components.Player;
import io.zerocontribution.winter.components.Stats;
import io.zerocontribution.winter.ui.UIManager;

public class PartyFrames extends Group {

    public PartyFrames() {
        ImmutableBag<Entity> knownPlayers = WinterGame.world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS);

        for (int i = 0; i < knownPlayers.size(); i++) {
            PartyFrame frame = new PartyFrame(knownPlayers.get(i));
            frame.setPosition(20, Gdx.graphics.getHeight() - (i * frame.getHeight()));
            addActor(frame);
            Log.info("Client", "New PlayerFrame");
        }
    }

    private class PartyFrame extends Table {
        public PartyFrame(final Entity entity) {
            Skin skin = UIManager.getInstance().getSkin();
            setSkin(skin);
            setSize(250, 50);

            Player player = entity.getComponent(Player.class);
            Stats stats = entity.getComponent(Stats.class);

            Label playerName = new Label(player.name, skin);
            playerName.setFontScale(0.8f);
            add(playerName).expandX();
            row();

            Label health = new Label(stats.health + "/" + stats.maxHealth, skin);
            health.setFontScale(0.5f);
            add(health).expandX();
        }
    }
}
