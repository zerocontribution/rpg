package io.zerocontribution.winter.client.screens;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.DeprecatedAssets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.systems.*;
import io.zerocontribution.winter.systems.client.*;
import io.zerocontribution.winter.utils.ClientGlobals;

/**
 * The GameScreen handles the lifecycle of the actual game play.
 *
 * It should not be involved with setting up the world, loading assets or configuring the game whatsoever. It's the
 * responsibility of previous screens (namely GameLoadScreen) to accomplish these tasks.
 */
public class GameScreen extends AbstractScreen {

    SpriteBatch spriteBatch;
    BitmapFont font;

    WinterGame game;
    World world;

    private AnimationRenderingSystem animationRenderingSystem;
    private MapRenderingSystem mapRenderingSystem;
    private CollisionDebugSystem collisionDebugSystem;
    private HudSystem hudSystem;
    private DebugHudSystem debugHudSystem;

    public GameScreen(WinterGame game, String map) {
        super(game);
        this.game = WinterGame.getInstance();

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setUseIntegerPositions(false);

        ClientGlobals.loadClientMap(map);
        DeprecatedAssets.loadImages();

        world = WinterGame.world; // TODO Remove local reference

        world.setSystem(new ClientNetworkSystem(game.gameClient.client, 1 / 33.0f));
        world.setSystem(new PingSystem(game.gameClient.client));

        // TODO Refactor these systems to not change entity states (e.g. setting State to DYING during combat)
        world.setSystem(new CameraSystem());
        world.setSystem(new PlayerInputSystem());
        world.setSystem(new ActionProcessingSystem());
        world.setSystem(new CombatProcessingSystem());
        world.setSystem(new ClientDamageProcessingSystem());
        world.setSystem(new ClientCollisionSystem());
        world.setSystem(new MovementSystem(ClientGlobals.currentMap));
        world.setSystem(new AnimationUpdatingSystem());
        world.setSystem(new ExpiredProcessingSystem());

        mapRenderingSystem = world.setSystem(new MapRenderingSystem(), true);
        animationRenderingSystem = world.setSystem(new AnimationRenderingSystem(spriteBatch), true);
        hudSystem = world.setSystem(new HudSystem(), true);

        if (Constants.DEBUG) {
            collisionDebugSystem = world.setSystem(new CollisionDebugSystem(), true);
            debugHudSystem = world.setSystem(new DebugHudSystem(game), true);
        }

        ClientGlobals.entityFactory.createMap(world, spriteBatch).addToWorld();

        WinterGame.gameClient.sendGameLoaded();

        world.initialize();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.setDelta(delta);
        world.process();

        mapRenderingSystem.process();
        animationRenderingSystem.process();
        hudSystem.process();

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
