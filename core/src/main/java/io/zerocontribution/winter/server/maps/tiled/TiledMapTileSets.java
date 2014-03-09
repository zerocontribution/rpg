package io.zerocontribution.winter.server.maps.tiled;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class TiledMapTileSets implements Iterable<TiledMapTileSet> {

    private Array<TiledMapTileSet> tileSets;

    public TiledMapTileSets() {
        tileSets = new Array<TiledMapTileSet>();
    }

    public TiledMapTileSet getTileSet(int index) {
        return tileSets.get(index);
    }

    public TiledMapTileSet getTileSet(String name) {
        for (TiledMapTileSet tileSet : tileSets) {
            if (name.equals(tileSet.getName())) {
                return tileSet;
            }
        }
        return null;
    }

    public void addTileSet(TiledMapTileSet tileSet) {
        tileSets.add(tileSet);
    }

    public void removeTileSet(int index) {
        tileSets.removeIndex(index);
    }

    public void removeTileSet(TiledMapTileSet tileSet) {
        tileSets.removeValue(tileSet, true);
    }

    public TiledMapTile getTile(int id) {
        for (TiledMapTileSet tileSet : tileSets) {
            TiledMapTile tile = tileSet.getTile(id);
            if (tile != null) {
                return tile;
            }
        }
        return null;
    }

    @Override
    public Iterator<TiledMapTileSet> iterator() {
        return tileSets.iterator();
    }

}
