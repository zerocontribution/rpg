package io.zerocontribution.winter.server.maps.tiled;

import com.badlogic.gdx.maps.MapProperties;

public interface TiledMapTile {

    public int getId();
    public void setId(int id);

    public MapProperties getProperties();

}
