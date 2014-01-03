package io.zerocontribution.winter.assets;

import java.util.ArrayList;

public class LevelsAsset {

    public int maxLevel;
    public ArrayList<LevelAsset> levels;

    public int getMaxExperienceForLevel(int level) {
        for (LevelAsset l : levels) {
            if (level == l.level) {
                return l.maxExperience;
            }
        }

        return -1;
    }

}
