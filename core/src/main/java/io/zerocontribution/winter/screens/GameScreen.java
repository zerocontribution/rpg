package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.systems.AnimationRenderingSystem;
import io.zerocontribution.winter.systems.CollisionDebugSystem;
import io.zerocontribution.winter.systems.DebugHudSystem;
import io.zerocontribution.winter.systems.MapRenderingSystem;

/**
 * The GameScreen handles the lifecycle of the actual game play.
 *
 * It should not be involved with setting up the world, loading assets or configuring the game whatsoever. It's the
 * responsibility of previous screens (namely GameLoadScreen) to accomplish these tasks.
 */
public class GameScreen implements Screen {

    WinterGame game;

    private AnimationRenderingSystem animationRenderingSystem;
    private MapRenderingSystem mapRenderingSystem;
    private CollisionDebugSystem collisionDebugSystem;
    private DebugHudSystem debugHudSystem;

    public GameScreen(WinterGame game) {
        this.game = game;

        animationRenderingSystem = game.world.getSystem(AnimationRenderingSystem.class);
        mapRenderingSystem = game.world.getSystem(MapRenderingSystem.class);

        if (Constants.DEBUG) {
            collisionDebugSystem = game.world.getSystem(CollisionDebugSystem.class);
            debugHudSystem = game.world.getSystem(DebugHudSystem.class);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.world.setDelta(delta);
        game.world.process();

        mapRenderingSystem.process();
        animationRenderingSystem.process();

        if (Constants.DEBUG) {
            collisionDebugSystem.process();
            debugHudSystem.process();
        }
    }

    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        WinterGame.gameClient.sendLogout();
    }

}
