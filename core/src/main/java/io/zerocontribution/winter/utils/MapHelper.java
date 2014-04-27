package io.zerocontribution.winter.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.server.maps.tiled.TiledMap;
import net.dermetfan.utils.libgdx.maps.MapUtils;

import java.util.Vector;

/**
 * Simple helper methods for Map math.
 *
 * @todo Cache vector results instead of creating new objects all the time.
 * @todo Fucking fuck.
 *
 * @link http://www.alcove-games.com/advanced-tutorials/isometric-tile-picking/
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

    public static Vector2 gridToScreen(float x, float y) {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) ClientGlobals.currentMap.getLayers().get("Ground");

        float tileHalfW = groundLayer.getTileWidth() / 2;
        float tileHalfH = groundLayer.getTileHeight() / 2;

        return new Vector2(
                (x - y) * tileHalfW,
                (x + y) * tileHalfH
        );
    }

    public static Vector2 screenToGrid(Camera camera, float x, float y) {
        Vector3 unprojected = new Vector3(x, y, 0);
        camera.unproject(unprojected);

        Vector3 point = MapUtils.toIsometricGridPoint(unprojected, Constants.TILE_WIDTH, Constants.TILE_HEIGHT);

        return new Vector2(
                (float) Math.floor(point.x),
                (float) Math.floor(point.y)
        );
    }

}