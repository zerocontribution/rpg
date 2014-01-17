package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.artemis.Entity;

public abstract class BaseComponent extends Component {

    public Object create(Entity entity) {
        return null;
    }

    public boolean isUpdated() {
        return false;
    }

}
