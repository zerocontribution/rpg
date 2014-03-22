package io.zerocontribution.winter.assets;

import java.util.HashMap;

public class EnemyAsset {

    /**
     * Internal identifier; unique
     */
    public String name;

    /**
     * UI-friendly name.
     */
    public String label;

    /**
     * RGBA
     */
    public float[] color;

    /**
     * width,height
     */
    public float[] bounds;

    /**
     * Ability IDs.
     */
    public int[] abilities;

    public String ai;

    public HashMap<String, Integer> baseStats = new HashMap<String, Integer>();

}
