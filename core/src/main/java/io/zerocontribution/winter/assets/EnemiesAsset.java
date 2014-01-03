package io.zerocontribution.winter.assets;

import java.util.ArrayList;

public class EnemiesAsset {
    public ArrayList<EnemyAsset> enemies;

    public EnemyAsset get(String name) {
        for (EnemyAsset e : enemies) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
