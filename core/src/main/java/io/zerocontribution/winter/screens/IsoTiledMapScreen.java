package io.zerocontribution.winter.screens;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.systems.*;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;
import io.zerocontribution.winter.systems.server.ServerNetworkSystem;
import io.zerocontribution.winter.utils.ClientGlobals;

/**
 * @deprecated Use GameScreen instead
 */
public class IsoTiledMapScreen implements Screen {

    private final World world;

    private AnimationRenderingSystem animationRenderingSystem;
    private MapRenderingSystem mapRenderer;
    private CollisionDebugSystem collisionDebugSystem;
    private DebugHudSystem debugHudSystem;

    public IsoTiledMapScreen(WinterGame game, boolean isServer, String localPlayerName) {
        Assets.loadConfigurations();
        Assets.loadMap("maps/isometric.tmx");
        Assets.loadImages();

        SpriteBatch spriteBatch = new SpriteBatch();

        // TODO Figure out what to do with this shitshow.
        WinterGame.world = new World();
        world = WinterGame.world;
        world.setManager(new GroupManager());
        world.setManager(new TagManager());

//        world.setSystem(ClientGlobals.network);

        // TODO: We don't want all of these on the client-side.
        world.setSystem(new FPSLoggingSystem());
        world.setSystem(new CameraSystem());
        world.setSystem(new PlayerInputSystem());
        world.setSystem(new AIProcessingSystem());
        world.setSystem(new ActionProcessingSystem());
        world.setSystem(new CombatProcessingSystem());
        world.setSystem(new DamageProcessingSystem());
        world.setSystem(new CollisionSystem());
        world.setSystem(new MovementSystem());
        world.setSystem(new AnimationUpdatingSystem());
        world.setSystem(new ExpiredProcessingSystem());

        mapRenderer = world.setSystem(new MapRenderingSystem(), true);
        animationRenderingSystem = world.setSystem(new AnimationRenderingSystem(spriteBatch), true);

        if (Constants.DEBUG) {
            collisionDebugSystem = world.setSystem(new CollisionDebugSystem(), true);
            debugHudSystem = world.setSystem(new DebugHudSystem(), true);
        }

        EntityFactory.createMap(world, spriteBatch).addToWorld();
        EntityFactory.createPlayer(world, "CannonFodder", 0, 0).addToWorld();
        EntityFactory.createEnemy(world, "player", 6, 6).addToWorld();

        world.initialize();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ClientGlobals.time = System.currentTimeMillis() + ClientGlobals.timeDiff;
        world.setDelta(delta);
        world.process();


        mapRenderer.process();
        animationRenderingSystem.process();

        if (Constants.DEBUG) {
            collisionDebugSystem.process();
            debugHudSystem.process();
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO: Get camera entity from world and update?
        // Might make more sense to create a CameraResizeSystem? Could be useful for resolution changes?
//        camera.viewportWidth = width;
//        camera.viewportHeight = height;
//        camera.update();
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
            // TODO: Leaving the level shouldn a player out. What about map changes or going to the lobby?
    }
}
