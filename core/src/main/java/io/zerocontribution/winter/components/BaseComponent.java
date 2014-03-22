package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.artemis.Entity;

public abstract class BaseComponent extends Component {

    protected boolean updated = true;

    public Object create(Entity entity) {
        return null;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated() {
        updated = true;
    }

    public void setUpdated(boolean flag) {
        updated = flag;
    }

}
