package io.zerocontribution.winter.combat;

import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.combat.abilities.PunchAbility;

// TODO Contains an identity map of abilities to query against.
public class Abilities {

    public static void loadAbilities() {}

    public static Ability get(int abilityId) {
        return new PunchAbility();
    }
}
