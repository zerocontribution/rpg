package io.zerocontribution.winter.systems;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.utils.ClientGlobals;
import io.zerocontribution.winter.utils.ServerGlobals;

import java.text.NumberFormat;
import java.util.ArrayList;

public class DebugHudSystem extends VoidEntitySystem {

    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private WinterGame game;

    public DebugHudSystem(WinterGame game) {
        this.game = game;
    }

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
        lines.add("Entities: active: " + world.getEntityManager().getActiveEntityCount() +
                "; created: " + world.getEntityManager().getTotalCreated() +
                "; deleted: " + world.getEntityManager().getTotalDeleted());

        if (game.isHost()) {
            lines.add("Server Entities: active: " + ServerGlobals.entitiesActive +
                    "; created: " + ServerGlobals.entitiesCreated +
                    "; deleted: " + ServerGlobals.entitiesDeleted);
        }

        NumberFormat format = NumberFormat.getInstance();
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        long allocatedMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        lines.add("Mem: free:" + format.format(freeMemory) + "; max:" + format.format(maxMemory) + "; allocated: " + format.format(allocatedMemory));

        for (int i = 0; i < lines.size(); i++) {
            font.draw(spriteBatch, lines.get(i), 20, 20 + (i * 20));
        }
    }

    @Override
    protected void end() {
        spriteBatch.end();
    }

}
