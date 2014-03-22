package io.zerocontribution.winter.assets;

import java.util.ArrayList;

public class ClassesAsset {
    public ArrayList<ClassAsset> classes;

    public ClassAsset get(String name) {
        for (ClassAsset c : classes) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        return null;
    }
}
