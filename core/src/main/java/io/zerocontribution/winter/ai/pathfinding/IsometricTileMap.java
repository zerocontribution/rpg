package io.zerocontribution.winter.ai.pathfinding;

import io.zerocontribution.winter.Pair;
import io.zerocontribution.winter.components.PairMap;
import io.zerocontribution.winter.server.maps.tiled.TiledMap;
import io.zerocontribution.winter.utils.GdxLogHelper;

/**
 * @todo Need to add support for adding blocking entities as well. This will need to be handled each tick.
 */
public class IsometricTileMap implements TileBasedMap {

    private int width;
    private int height;
    private PairMap tilePairMap;

    public IsometricTileMap(TiledMap map, PairMap tilePairMap) {
        width = map.getProperties().get("width", Integer.class);
        height = map.getProperties().get("height", Integer.class);
        this.tilePairMap = tilePairMap;
    }

    @Override
    public int getWidthInTiles() {
        return width;
    }

    @Override
    public int getHeightInTiles() {
        return height;
    }

    @Override
    public void pathFinderVisited(int x, int y) {
        GdxLogHelper.debug("astar-visited", new StringBuilder().append(x).append(",").append(y).toString());
    }

    @Override
    public boolean blocked(Mover mover, int x, int y) {
        return tilePairMap.map.containsKey(Pair.get(x, y));
    }

    @Override
    public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
        return 1;
    }

}
