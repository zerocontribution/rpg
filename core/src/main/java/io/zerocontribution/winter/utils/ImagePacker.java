package io.zerocontribution.winter.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

/**
 * @todo Nifty, but not very good in its present state. Just manually compile assets for now.
 */
public class ImagePacker {

    public static void run() {
        TexturePacker2.Settings settings = new TexturePacker2.Settings();
        settings.paddingX = settings.paddingY = 2;
        settings.stripWhitespaceX = settings.stripWhitespaceY = false;
        settings.minHeight = 1024;
        settings.minWidth = 1024;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.Linear;
        TexturePacker2.process(settings, "textures-original", "assets/textures", "pack");
    }
}
