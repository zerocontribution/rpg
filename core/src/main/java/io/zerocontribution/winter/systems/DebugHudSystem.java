package io.zerocontribution.winter.systems;

import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.Constants;

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
        font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
        font.draw(spriteBatch, "Active entities: " + world.getEntityManager().getActiveEntityCount(), 20, 40);
        font.draw(spriteBatch, "Total created: " + world.getEntityManager().getTotalCreated(), 20, 60);
        font.draw(spriteBatch, "Total deleted: " + world.getEntityManager().getTotalDeleted(), 20, 80);
    }

    @Override
    protected void end() {
        spriteBatch.end();
    }

}
