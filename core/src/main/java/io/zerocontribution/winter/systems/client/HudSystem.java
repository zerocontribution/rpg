package io.zerocontribution.winter.systems.client;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.zerocontribution.winter.ui.UIManager;
import io.zerocontribution.winter.ui.hud.ActionBar;
import io.zerocontribution.winter.ui.hud.ScrollingCombatText;

/**
 * This seems lacking, but it will have more functionality as time goes on.
 *
 * @todo The HudSystem should probably be on an interval: There's no reason to render the HUD every tick.
 */
public class HudSystem extends VoidEntitySystem {

    @Override
    protected void initialize() {
        Stage stage = UIManager.getInstance().getStage();
        stage.addActor(new ActionBar());
        stage.addActor(new ScrollingCombatText());
    }

    protected void processSystem() {
        UIManager.getInstance().render(world.delta);
    }

}
