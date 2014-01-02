package io.zerocontribution.winter;

// TOOD Read all of this from a levels.xml resource instead. No calculations should be done for any of this.
public class Levels {

    public static int getMaxLevel() {
        return 20;
    }

    public static int getLevelExperience(int level) {
        // TODO Change logic to disallow getting level experience once capped.
        if (level == getMaxLevel()) {
            return 100000000;
        }
        double levelExp = 300;
        for (int i = 1; i < level; i++) {
            levelExp *= 0.1;
        }
        return (int) Math.ceil(levelExp);
    }

}
