package io.zerocontribution.winter;

import com.badlogic.gdx.Gdx;

public class Constants {

    final public static boolean DEBUG = true;

    final public static float PLAYER_SPEED = 240; // 120 is roughly run speed.

    final public static int CAMERA_WIDTH = Gdx.graphics.getWidth();
    final public static int CAMERA_HEIGHT = Gdx.graphics.getHeight();

    final public static String MAP_COLLISIONS_LAYER = "Collisions";

    final public static float TILE_WIDTH = 64;
    final public static float TILE_HEIGHT = 32;

    public class Groups {
        final public static String ACTORS = "actors";
        final public static String PLAYER_AVATAR = "player avatar";
        final public static String BLOCKING_TILES = "blocking tiles";
    }

    public class Tags {
        final public static String VIEW = "view";
        final public static String PLAYER = "player";
    }

}
