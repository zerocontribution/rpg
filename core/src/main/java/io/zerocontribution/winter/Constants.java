package io.zerocontribution.winter;

import com.badlogic.gdx.Gdx;

public class Constants {

    final public static boolean DEBUG = true;

    final public static String DEFAULT_PLAYER_NAME = "Buttmuncher";
    final public static float PLAYER_SPEED = 240; // 120 is roughly run speed.

    final public static int CAMERA_WIDTH = Gdx.graphics.getWidth();
    final public static int CAMERA_HEIGHT = Gdx.graphics.getHeight();

    final public static float TILE_WIDTH = 64;
    final public static float TILE_HEIGHT = 32;
    final public static String TILE_OBSTACLE = "obstacle";

    public class UI {
        final public static String DEFAULT_PLAYER_NAME = "Character Name";
    }

    public class Groups {
        /**
         * All Entities that should be sent to the client.
         */
        final public static String CLIENT = "client"; //This is a Server-only group; should be put in own class.

        final public static String ACTORS = "actors";
        final public static String PLAYERS = "players";
        final public static String BLOCKING_TILES = "blocking tiles";
        final public static String NPC_ATTACKS = "npc attacks";
        final public static String ENEMIES = "enemies";
    }

    public class Tags {
        final public static String VIEW = "view";

        // TODO: Remove. Direct access now from ClientGlobals.player
        final public static String LOCAL_PLAYER = "player";
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
            final public static String DYING = "playerDying";
        }
    }

}
