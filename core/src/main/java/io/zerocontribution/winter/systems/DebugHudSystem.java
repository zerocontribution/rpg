package io.zerocontribution.winter.systems;

import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.utils.ClientGlobals;

import java.util.ArrayList;

public class DebugHudSystem extends VoidEntitySystem {

    private SpriteBatch spriteBatch;
    private BitmapFont font;

    @Override
    protected void initialize() {
        spriteBatch = new SpriteBatch();

        font = new BitmapFont();
        font.setUseIntegerPositions(false);
    }

    @Override
    protected void begin() {
        spriteBatch.begin();
    }

    @Override
    protected void processSystem() {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("Ping: " + ClientGlobals.ping);
        lines.add("FPS: " + Gdx.graphics.getFramesPerSecond());
        lines.add("Active entities: " + world.getEntityManager().getActiveEntityCount());
        lines.add("Total created: " + world.getEntityManager().getTotalCreated());
        lines.add("Total deleted: " + world.getEntityManager().getTotalDeleted());

        for (int i = 0; i < lines.size(); i++) {
            font.draw(spriteBatch, lines.get(i), 20, 20 + (i * 20));
        }
    }

    @Override
    protected void end() {
        spriteBatch.end();
    }

}
