package io.zerocontribution.winter.server.maps.tiled;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.IntMap;

import java.util.Iterator;

public class TiledMapTileSet implements Iterable<TiledMapTile> {

    private String name;
    private IntMap<TiledMapTile> tiles;
    private MapProperties properties;

    public TiledMapTileSet() {
        tiles = new IntMap<TiledMapTile>();
        properties = new MapProperties();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MapProperties getProperties() {
        return properties;
    }

    public TiledMapTile getTile(int id) {
        return tiles.get(id);
    }

    public void putTile(int id, TiledMapTile tile) {
        tiles.put(id, tile);
    }

    public void removeTile(int id) {
        tiles.remove(id);
    }

    public int size() {
        return tiles.size;
    }

    @Override
    public Iterator<TiledMapTile> iterator() {
        return tiles.values().iterator();
    }

}
