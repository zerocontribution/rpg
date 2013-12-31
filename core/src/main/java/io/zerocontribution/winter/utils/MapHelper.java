package io.zerocontribution.winter.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.Assets;

/**
 * Simple helper methods for Map math.
 *
 * @todo Cache vector results instead of creating new objects all the time.
 */
public class MapHelper {

    public static Vector2 gridToWorld(float x, float y) {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) Assets.currentMap.getLayers().get("Ground");

        float tileHalfW = groundLayer.getTileWidth() / 2;
        float tileHalfH = groundLayer.getTileHeight() / 2;

        return new Vector2(
                (x * tileHalfW) + (y * tileHalfW),
                (y * tileHalfH) - (x * tileHalfH)
        );
    }

    public static Vector2 worldToGrid(float x, float y) {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) Assets.currentMap.getLayers().get("Ground");

        float tileHalfW = groundLayer.getTileWidth();
        float tileHalfH = groundLayer.getTileHeight();

        // TODO This is most certainly incorrect.
        return new Vector2(
                (float) Math.floor((x / tileHalfW) - (y / tileHalfW)),
                (float) Math.floor((y / tileHalfH) + (x / tileHalfH))
        );
    }

}