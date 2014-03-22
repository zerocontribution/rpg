package io.zerocontribution.winter.utils;

import com.badlogic.gdx.Gdx;

public class GdxLogHelper {

    public static void log(Class tag, String message) {
        Gdx.app.log(tag.getSimpleName(), message);
    }

    public static void log(String tag, Object message) {
        Gdx.app.log(tag, String.valueOf(message));
    }

    public static void log(String tag, int message) {
        Gdx.app.log(tag, String.valueOf(message));
    }

    public static void log(String tag, float message) {
        Gdx.app.log(tag, String.valueOf(message));
    }

    public static void log(String tag, double message) {
        Gdx.app.log(tag, String.valueOf(message));
    }

    public static void log(String tag, boolean message) {
        Gdx.app.log(tag, String.valueOf(message));
    }

    public static void debug(String tag, String message) {
        Gdx.app.debug(tag, message);
    }

    public static void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }

}
