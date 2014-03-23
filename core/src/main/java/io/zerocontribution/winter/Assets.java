package io.zerocontribution.winter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import io.zerocontribution.winter.Constants.Animations.Player;
import io.zerocontribution.winter.assets.*;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.spawners.IntervalSpawner;
import io.zerocontribution.winter.spawners.Spawner;
import io.zerocontribution.winter.spawners.WaveSpawner;

/**
 * @todo This class really needs some love. Too much work to maintain it.
 * @todo Refactor to using XML configurations of entities & maps.
 */
public class Assets {

    public static LevelsAsset levels;
    public static EnemiesAsset enemies;
    public static AbilitiesAsset abilities;
    public static MapsAsset maps;

    private static Animation playerRunUp;
    private static Animation playerRunUpRight;
    private static Animation playerRunUpLeft;
    private static Animation playerRunDown;
    private static Animation playerRunDownRight;
    private static Animation playerRunDownLeft;
    private static Animation playerRunRight;
    private static Animation playerRunLeft;
    private static Animation playerDying;

    public static void loadConfigurations() {
        Json json = new Json();
        json.setElementType(LevelsAsset.class, "levels", LevelAsset.class);
        json.setElementType(EnemiesAsset.class, "enemies", EnemyAsset.class);
        json.setElementType(AbilitiesAsset.class, "abilities", Ability.class);
        json.setElementType(MapsAsset.class, "maps", MapAsset.class);

        json.addClassTag("intervalSpawner", IntervalSpawner.class);
        json.addClassTag("waveSpawner", WaveSpawner.class);

        levels = json.fromJson(LevelsAsset.class, Gdx.files.internal("levels.json").read());
        enemies = json.fromJson(EnemiesAsset.class, Gdx.files.internal("enemies.json").read());
        abilities = json.fromJson(AbilitiesAsset.class, Gdx.files.internal("abilities.json").read());
        maps = json.fromJson(MapsAsset.class, Gdx.files.internal("maps.json").read());
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
        playerDying = new Animation(Constants.Animations.ENTITY_RUN_FRAME_LENGTH, atlas.createSprites(Player.DYING), Animation.NORMAL);
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
        } else if (name.equals(Player.DYING)) {
            return playerDying;
        } else {
            throw new IllegalArgumentException("Invalid animation name: " + name);
        }
    }

    public static TextureRegion getAnimationFrame(String name, float stateTime) {
        return getAnimation(name).getKeyFrame(stateTime);
    }

}
