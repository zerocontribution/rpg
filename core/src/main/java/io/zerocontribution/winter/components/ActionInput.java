package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.artemis.Entity;

public class ActionInput extends Component {
    public String group;
    public int abilityId; // TODO This should be loaded from the assets (XML, or whatever)
    public Entity target;

    public ActionInput(String group, int abilityId) {
        this.group = group;
        this.abilityId = abilityId;
    }

    public ActionInput(String group, int abilityId, Entity target) {
        this.group = group;
        this.abilityId = abilityId;
        this.target = target;
    }

}
