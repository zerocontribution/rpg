package io.zerocontribution.winter;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Winter Game";
        config.useGL20 = true;
        config.width = 1200;
        config.height = 720;
        new LwjglApplication(new WinterGame(), config);
    }
}
