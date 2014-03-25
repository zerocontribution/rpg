package io.zerocontribution.winter.systems.client;

import com.artemis.systems.VoidEntitySystem;
import io.zerocontribution.winter.ui.UIManager;
import io.zerocontribution.winter.ui.hud.ActionBar;

/**
 * This seems lacking, but it will have more functionality as time goes on.
 */
public class HudSystem extends VoidEntitySystem {

    @Override
    protected void initialize() {
        UIManager.getInstance().getStage().addActor(new ActionBar());
    }

    protected void processSystem() {
        UIManager.getInstance().render(world.delta);
    }

}
