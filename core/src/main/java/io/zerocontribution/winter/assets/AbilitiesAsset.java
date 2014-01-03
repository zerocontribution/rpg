package io.zerocontribution.winter.assets;

import io.zerocontribution.winter.combat.abilities.Ability;

import java.util.ArrayList;

public class AbilitiesAsset {
    private ArrayList<Ability> abilities;

    public Ability get(int id) {
        for (Ability ability : abilities) {
            if (ability.id == id) {
                return ability;
            }
        }
        return null;
    }

}
