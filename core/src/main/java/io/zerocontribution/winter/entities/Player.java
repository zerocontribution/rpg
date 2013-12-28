package io.zerocontribution.winter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

    final public static String OBSTACLE_KEY = "obstacle";

    private Vector2 velocity = new Vector2();
    private float speed = 60 * 2, animationTime = 0, increment;
    private final Animation still;

    private final TiledMapTileLayer collisionLayer;

    public Player(Animation still, TiledMapTileLayer collisionLayer) {
        super(still.getKeyFrame(0));
        this.still = still;
        this.collisionLayer = collisionLayer;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void update(float delta) {
        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;

        setX(getX() + velocity.x * delta);

        increment = collisionLayer.getTileWidth();
        increment = getWidth() < increment ? getWidth() / 2 : increment / 2;

        if (velocity.x < 0) {
            collisionX = collidesLeft();
        } else if (velocity.x > 0) {
            collisionX = collidesRight();
        }

        if (collisionX) {
            setX(oldX);
            velocity.x = 0;
        }

        setY(getY() + velocity.y * delta);

        increment = collisionLayer.getTileHeight();
        increment = getHeight() < increment ? getHeight() / 2 : increment / 2;

        if (velocity.y < 0) {
            collisionY = collidesBottom();
        } else {
            collisionY = collidesTop();
        }

        if (collisionY) {
            setY(oldY);
            velocity.y = 0;
        }

        animationTime += delta;
        setRegion(still.getKeyFrame(animationTime));
    }

    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(OBSTACLE_KEY);
    }

    private boolean collidesRight() {
        for (float step = 0; step <= getHeight(); step += increment) {
            if (isCellBlocked(getX() + getWidth(), getY() + step)) {
                return true;
            }
        }
        return false;
    }

    private boolean collidesLeft() {
        for (float step = 0; step <= getHeight(); step += increment) {
            if (isCellBlocked(getX(), getY() + step)) {
                return true;
            }
        }
        return false;
    }

    private boolean collidesTop() {
        for (float step = 0; step <= getWidth(); step += increment) {
            if (isCellBlocked(getX() + step, getY() + getHeight())) {
                return true;
            }
        }
        return false;
    }

    private boolean collidesBottom() {
        for (float step = 0; step <= getWidth(); step += increment) {
            if (isCellBlocked(getX(), getY() + getHeight())) {
                return true;
            }
        }
        return false;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                velocity.y = speed;
                break;
            case Keys.S:
                velocity.y = -speed;
                break;
            case Keys.A:
                velocity.x = -speed;
                break;
            case Keys.D:
                velocity.x = speed;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.S:
                velocity.y = 0;
                break;
            case Keys.A:
            case Keys.D:
                velocity.x = 0;
                break;
        }
        animationTime = 0;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
