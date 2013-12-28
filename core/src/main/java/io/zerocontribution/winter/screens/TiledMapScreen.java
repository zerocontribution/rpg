package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.zerocontribution.winter.entities.Player;
import io.zerocontribution.winter.maps.IsometricTiledMapHelper;

public class TiledMapScreen implements Screen {

    private TiledMap map;
    private IsometricTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private TextureAtlas playerAtlas;
    private Player player;

    private int[] background = new int[] {0}, foreground = new int[] {1};

    private BitmapFont font;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        IsometricTiledMapHelper mapHelper = new IsometricTiledMapHelper(map);

        renderer.setView(camera);

        renderer.render(background);

        Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        SpriteBatch spriteBatch = renderer.getSpriteBatch();
        spriteBatch.begin();

        player.draw(spriteBatch);
        spriteBatch.end();

        player.drawDebug(camera);

        renderer.render(foreground);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void show() {
        font = new BitmapFont();

        map = new TmxMapLoader().load("maps/isometric.tmx");

        renderer = new IsometricTiledMapRenderer(map);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playerAtlas = new TextureAtlas("img/player/player.atlas");
        Animation still;
        still = new Animation(1 / 2f, playerAtlas.findRegions("down"));
        still.setPlayMode(Animation.LOOP);

        IsometricTiledMapHelper mapHelper = new IsometricTiledMapHelper(map);

        player = new Player(still, mapHelper.getCollisionLayer());

        Vector2 spawnPoint = mapHelper.convertToScreen(0, 1);

        player.setPosition(spawnPoint.x, spawnPoint.y);
        camera.position.set(player.getX(), player.getY(), 0);

        Gdx.input.setInputProcessor(player);
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
        map.dispose();
        renderer.dispose();
        playerAtlas.dispose();
    }
}
