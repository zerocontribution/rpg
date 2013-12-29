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

    private SpriteRenderSystem spriteRenderSystem;
    private AnimationRenderSystem animationRenderSystem;
    private MapRenderingSystem mapRenderer;
    private CollisionDebugSystem collisionDebugSystem;

    public IsoTiledMapScreen() {
        Assets.loadMap("maps/isometric.tmx");

        world = new World();
        world.setManager(new GroupManager());
        world.setManager(new TagManager());

        world.setSystem(new FPSLoggingSystem());
        world.setSystem(new CameraSystem());
        world.setSystem(new PlayerInputSystem());
        world.setSystem(new CollisionSystem());
        world.setSystem(new MovementSystem());

        mapRenderer = world.setSystem(new MapRenderingSystem(), true);
        spriteRenderSystem = world.setSystem(new SpriteRenderSystem(), true);
        animationRenderSystem = world.setSystem(new AnimationRenderSystem(), true);

        if (Constants.DEBUG) {
            collisionDebugSystem = world.setSystem(new CollisionDebugSystem(), true);
        }

        EntityFactory.createMap(world, new SpriteBatch()).addToWorld();
        EntityFactory.createPlayer(world, 0, 0).addToWorld();

        world.initialize();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.setDelta(delta);
        world.process();

        mapRenderer.process();
        spriteRenderSystem.process();
        animationRenderSystem.process();

        if (Constants.DEBUG) {
            collisionDebugSystem.process();
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO: Get camera entity from world and update?
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
