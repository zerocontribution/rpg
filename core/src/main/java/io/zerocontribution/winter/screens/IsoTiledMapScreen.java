package io.zerocontribution.winter.screens;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.systems.*;

public class IsoTiledMapScreen implements Screen {

    private final World world;

    private AnimationRenderingSystem animationRenderingSystem;
    private MapRenderingSystem mapRenderer;
    private CollisionDebugSystem collisionDebugSystem;

    public IsoTiledMapScreen() {
        Assets.loadMap("maps/isometric.tmx");
        Assets.loadImages();

        SpriteBatch spriteBatch = new SpriteBatch();

        world = new World();
        world.setManager(new GroupManager());
        world.setManager(new TagManager());

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
        }

        EntityFactory.createMap(world, spriteBatch).addToWorld();
        EntityFactory.createPlayer(world, 0, 0).addToWorld();
        EntityFactory.createEnemy(world, 6, 6).addToWorld();

        world.initialize();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.setDelta(delta);
        world.process();

        mapRenderer.process();
        animationRenderingSystem.process();

        if (Constants.DEBUG) {
            collisionDebugSystem.process();
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
    }
}
