package io.zerocontribution.winter.utils;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.server.maps.tiled.TiledMap;

/**
 * Simple helper methods for Map math.
 *
 * @todo Cache vector results instead of creating new objects all the time.
 * @todo Fucking fuck.
 */
public class MapHelper {

    public static Vector2 gridToWorld(Map map, float x, float y) {
        float tileHalfW;
        float tileHalfH;

        if (map instanceof TiledMap) {
            io.zerocontribution.winter.server.maps.tiled.TiledMapTileLayer groundLayer = (io.zerocontribution.winter.server.maps.tiled.TiledMapTileLayer) map.getLayers().get("Ground");

            tileHalfW = groundLayer.getTileWidth() / 2;
            tileHalfH = groundLayer.getTileHeight() / 2;

        } else {
            TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("Ground");

            tileHalfW = groundLayer.getTileWidth() / 2;
            tileHalfH = groundLayer.getTileHeight() / 2;
        }

        return new Vector2(
                (x * tileHalfW) + (y * tileHalfW),
                (y * tileHalfH) - (x * tileHalfH)
        );
    }

    public static Vector2 gridToWorld(float x, float y) {
        Log.warn("MapHelper", "gridToWorld(float, float) deprecated!");
        return gridToWorld(ClientGlobals.currentMap, x, y);
    }

    public static Vector2 worldToGrid(Map map, float x, float y) {
        float tileHalfW;
        float tileHalfH;

        if (map instanceof TiledMap) {
            io.zerocontribution.winter.server.maps.tiled.TiledMapTileLayer groundLayer = (io.zerocontribution.winter.server.maps.tiled.TiledMapTileLayer) map.getLayers().get("Ground");

            tileHalfW = groundLayer.getTileWidth() / 2;
            tileHalfH = groundLayer.getTileHeight() / 2;

        } else {
            TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("Ground");

            tileHalfW = groundLayer.getTileWidth() / 2;
            tileHalfH = groundLayer.getTileHeight() / 2;
        }

        return new Vector2(
                (float) Math.floor((x / tileHalfW - y / tileHalfH) / 2),
                (float) Math.floor((y / tileHalfH + x / tileHalfW) / 2)
        );
    }

    public static Vector2 worldToGrid(float x, float y) {
//        Log.warn("MapHelper", "worldToGrid(float, float) deprecated!");
        return worldToGrid(ClientGlobals.currentMap, x, y);
    }

    public static Vector2 gridToScreen(float x, float y) {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) ClientGlobals.currentMap.getLayers().get("Ground");

        float tileHalfW = groundLayer.getTileWidth() / 2;
        float tileHalfH = groundLayer.getTileHeight() / 2;

        return new Vector2(
                (x - y) * tileHalfW,
                (x + y) * tileHalfH
        );
    }

    public static Vector2 screenToGrid(float x, float y) {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) ClientGlobals.currentMap.getLayers().get("Ground");

        float tileHalfW = groundLayer.getTileWidth() / 2;
        float tileHalfH = groundLayer.getTileHeight() / 2;

        // TODO Not vetted.
        return new Vector2(
                (float) Math.floor((x / tileHalfW + y / tileHalfH) / 2),
                (float) Math.floor((y / tileHalfH - (x / tileHalfW)) / 2)
        );
    }

}