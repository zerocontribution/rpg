package io.zerocontribution.winter.systems.client;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import io.zerocontribution.winter.input.InputManager;
import io.zerocontribution.winter.ui.UIManager;
import io.zerocontribution.winter.ui.groups.PartyFrames;
import io.zerocontribution.winter.ui.hud.ActionBar;
import io.zerocontribution.winter.ui.hud.InGameMenu;
import io.zerocontribution.winter.ui.hud.InGameOptions;
import io.zerocontribution.winter.ui.hud.ScrollingCombatText;
import io.zerocontribution.winter.ui.windows.CharacterWindow;

/**
 * This seems lacking, but it will have more functionality as time goes on.
 *
 * @todo The HudSystem should probably be on an interval: There's no reason to render the HUD every tick.
 */
public class HudSystem extends VoidEntitySystem {

    Stage stage;

    private InGameMenu inGameMenu;
    private InGameOptions inGameOptions;
    private CharacterWindow characterWindow;

    @Override
    protected void initialize() {
        stage = UIManager.getInstance().getStage();
        stage.addActor(new ActionBar());
//        stage.addActor(new PartyFrames());
        stage.addActor(new ScrollingCombatText());

        inGameMenu = new InGameMenu();
        inGameOptions = new InGameOptions();
        characterWindow = new CharacterWindow();
        characterWindow.setPosition(Gdx.graphics.getWidth() - characterWindow.getWidth() - 20, 200);
    }

    protected void processSystem() {
        UIManager.getInstance().render(world.delta);
        Table.drawDebug(stage);
    }

    public void toggleInGameMenu() {
        toggleActor(inGameMenu, true);
    }

    public void toggleInGameOptions() {
        toggleActor(inGameOptions, false);
    }

    public void toggleCharacterWindow() {
        toggleActor(characterWindow, false);
    }

    private void toggleActor(Actor actor, boolean isInputProcessor) {
        if (!actor.hasParent()) {
            UIManager.getInstance().getStage().addActor(actor);
            if (isInputProcessor) {
                InputManager.getInputProcessor().addProcessor((InputProcessor) actor);
            }
        } else {
            actor.remove();
            if (isInputProcessor) {
                InputManager.getInputProcessor().addProcessor((InputProcessor) actor);
            }
        }
    }
}
