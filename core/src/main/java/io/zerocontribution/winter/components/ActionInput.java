package io.zerocontribution.winter.components;

import com.artemis.Entity;

public class ActionInput extends BaseComponent {
    public int abilityId; // TODO This should be loaded from the assets (XML, or whatever)
    public Entity target;

    public ActionInput(int abilityId) {
        this.abilityId = abilityId;
    }

    public ActionInput(int abilityId, Entity target) {
        this.abilityId = abilityId;
        this.target = target;
    }

}
