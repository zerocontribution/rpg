package io.zerocontribution.winter.screens;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.systems.*;

public class IsoTiledMapScreen implements Screen {

    private final World world;
    private final OrthographicCamera camera;

    private SpriteRenderSystem spriteRenderSystem;
    private AnimationRenderSystem animationRenderSystem;

    public IsoTiledMapScreen() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        world = new World();
        world.setManager(new GroupManager());

        world.setSystem(new FPSLoggingSystem());
        world.setSystem(new PlayerInputSystem());
        world.setSystem(new MovementSystem());

        spriteRenderSystem = world.setSystem(new SpriteRenderSystem(camera), true);
        animationRenderSystem = world.setSystem(new AnimationRenderSystem(camera), true);

        world.initialize();

        EntityFactory.createPlayer(world, 0, 0).addToWorld();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        world.setDelta(delta);
        world.process();

        spriteRenderSystem.process();
        animationRenderSystem.process();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
