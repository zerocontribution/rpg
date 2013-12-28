package io.zerocontribution.winter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.entities.Player;
import io.zerocontribution.winter.maps.TiledMapHelper;

public class TiledMapScreen implements Screen {

    private TiledMap map;
    private IsometricTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private TextureAtlas playerAtlas;
    private Player player;

    private int[] background = new int[] {0}, foreground = new int[] {1};

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        renderer.setView(camera);

        renderer.render(background);

        renderer.getSpriteBatch().begin();
        player.draw(renderer.getSpriteBatch());
        renderer.getSpriteBatch().end();

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
        map = new TmxMapLoader().load("maps/isometric.tmx");

        renderer = new IsometricTiledMapRenderer(map);

        camera = new OrthographicCamera();

        playerAtlas = new TextureAtlas("img/player/player.atlas");
        Animation still;
        still = new Animation(1 / 2f, playerAtlas.findRegions("down"));
        still.setPlayMode(Animation.LOOP);

        player = new Player(still, (TiledMapTileLayer) map.getLayers().get(0));

        TiledMapHelper mapHelper = new TiledMapHelper(map);
        Vector2 spawnPoint = mapHelper.toScreenCoords(mapHelper.getSpawnPoint());
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
