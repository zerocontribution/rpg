package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.utils.ObjectMap;
import io.zerocontribution.winter.Delay;
import io.zerocontribution.winter.utils.GdxLogHelper;

public class Actor extends Component {
    public int primaryAttack = 1; // TODO

    // abilityID: cooldown delta
    public ObjectMap<Integer, Delay> abilities = new ObjectMap<Integer, Delay>();

    public Entity currentTarget;

    public boolean hasAbility(int abilityId) {
        return abilities.containsKey(abilityId);
    }

    public boolean isAbilityInCooldown(int abilityId) {
        return abilities.get(abilityId).isElapsed();
    }

    public void resetAbilityCooldown(int abilityId) {
        abilities.get(abilityId).reset();
    }

}
