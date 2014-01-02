package io.zerocontribution.winter;

import com.badlogic.gdx.Gdx;

public class Constants {

    final public static boolean DEBUG = true;

    final public static float PLAYER_SPEED = 240; // 120 is roughly run speed.

    final public static int CAMERA_WIDTH = Gdx.graphics.getWidth();
    final public static int CAMERA_HEIGHT = Gdx.graphics.getHeight();

    final public static float TILE_WIDTH = 64;
    final public static float TILE_HEIGHT = 32;
    final public static String TILE_OBSTACLE = "obstacle";

    public class Groups {
        final public static String ACTORS = "actors";
        final public static String PLAYERS = "players";
        final public static String BLOCKING_TILES = "blocking tiles";
        final public static String NPC_ATTACKS = "npc attacks";
        final public static String ENEMIES = "enemies";
    }

    public class Tags {
        final public static String VIEW = "view";
        final public static String PLAYER = "player"; // TODO Probably don't want this... maybe a tag for LOCAL_PLAYER?
    }

    public class Animations {
        final public static int ENTITY_RUN_FRAME_LENGTH = 1;

        public class Player {
            final public static String RUN_UP = "playerRunUp";
            final public static String RUN_UPRIGHT = "playerRunUpRight";
            final public static String RUN_UPLEFT = "playerRunUpLeft";
            final public static String RUN_DOWN = "playerRunDown";
            final public static String RUN_DOWNRIGHT = "playerRunDownRight";
            final public static String RUN_DOWNLEFT = "playerRunDownLeft";
            final public static String RUN_RIGHT = "playerRunRight";
            final public static String RUN_LEFT = "playerRunLeft";
        }
    }

}
