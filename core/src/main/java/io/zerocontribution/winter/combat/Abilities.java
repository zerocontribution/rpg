package io.zerocontribution.winter.combat;

import io.zerocontribution.winter.DeprecatedAssets;
import io.zerocontribution.winter.combat.abilities.Ability;

// TODO Contains an identity map of abilities to query against.
public class Abilities {

    public static void loadAbilities() {}

    public static Ability get(int abilityId) {
        return DeprecatedAssets.abilities.get(abilityId);
    }
}
