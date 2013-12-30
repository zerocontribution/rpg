package io.zerocontribution.winter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.zerocontribution.winter.Constants.Animations.Player;

/**
 * @todo This class really needs some love. Too much work to maintain it.
 * @todo Refactor to using XML configurations of entities & maps.
 */
public class Assets {

    public static TiledMap currentMap;

    private static Animation playerRunUp;
    private static Animation playerRunUpRight;
    private static Animation playerRunUpLeft;
    private static Animation playerRunDown;
    private static Animation playerRunDownRight;
    private static Animation playerRunDownLeft;
    private static Animation playerRunRight;
    private static Animation playerRunLeft;

    public static void loadMap(String mapPath) {
        currentMap = new TmxMapLoader().load(mapPath);
    }

    public static void loadImages() {
        TextureAtlas atlas = new TextureAtlas("packs/player.pack");

        playerRunUp = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_UP), Animation.LOOP);
        playerRunUpRight = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_UPRIGHT), Animation.LOOP);
        playerRunUpLeft = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_UPLEFT), Animation.LOOP);
        playerRunDown = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_DOWN), Animation.LOOP);
        playerRunDownRight = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_DOWNRIGHT), Animation.LOOP);
        playerRunDownLeft = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_DOWNLEFT), Animation.LOOP);
        playerRunRight = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_RIGHT), Animation.LOOP);
        playerRunLeft = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.RUN_LEFT), Animation.LOOP);
    }

    public static Animation getAnimation(String name) {
        if (name.equals(Player.RUN_UP)) {
            return playerRunUp;
        } else if (name.equals(Player.RUN_UPRIGHT)) {
            return playerRunUpRight;
        } else if (name.equals(Player.RUN_UPLEFT)) {
            return playerRunUpLeft;
        } else if (name.equals(Player.RUN_DOWN)) {
            return playerRunDown;
        } else if (name.equals(Player.RUN_DOWNRIGHT)) {
            return playerRunDownRight;
        } else if (name.equals(Player.RUN_DOWNLEFT)) {
            return playerRunDownLeft;
        } else if (name.equals(Player.RUN_RIGHT)) {
            return playerRunRight;
        } else if (name.equals(Player.RUN_LEFT)) {
            return playerRunLeft;
        } else {
            throw new IllegalArgumentException("Invalid animation name: " + name);
        }
    }

    public static TextureRegion getAnimationFrame(String name, float stateTime) {
        return getAnimation(name).getKeyFrame(stateTime);
    }

}
