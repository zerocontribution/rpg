package io.zerocontribution.winter.assets;

import java.util.ArrayList;

public class MapsAsset {
    ArrayList<MapAsset> maps;

    public MapAsset get(String name) {
        for (MapAsset m : maps) {
            if (m.name.equals(name)) {
                return m;
            }
        }
        return null;
    }
}
