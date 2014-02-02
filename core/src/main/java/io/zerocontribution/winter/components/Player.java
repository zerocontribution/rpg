package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ObjectMap;
import io.zerocontribution.winter.combat.abilities.Ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player extends Component {

    public String group = "player1"; // TODO identifier for the ECS to group player actions, etc.

    public List<Integer> activeAbilities = new ArrayList<Integer>(5);

    public ObjectMap<Integer, Ability> abilityMap = new ObjectMap<Integer, Ability>();

    public void bindAbility(Ability ability, int keyCode) {
        if (abilityMap.containsValue(ability, false)) {
            abilityMap.remove(abilityMap.findKey(ability, false));
        }
        abilityMap.put(keyCode, ability);
    }

    public Ability getAbilityByKey(int key) {
        return abilityMap.get(key);
    }

    public Ability getAbilityById(int id) {
        for (Ability ability : abilityMap.values()) {
            if (ability.id == id) {
                return ability;
            }
        }
        return null;
    }

}
