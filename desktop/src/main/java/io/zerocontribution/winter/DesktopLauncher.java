package io.zerocontribution.winter;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Winter Game";
        config.width = 1600;
        config.height = 900;
        new LwjglApplication(new WinterGame(), config);
    }
}
