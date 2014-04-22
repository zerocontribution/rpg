package io.zerocontribution.winter.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.client.screens.MenuScreen;
import io.zerocontribution.winter.systems.client.HudSystem;
import io.zerocontribution.winter.ui.UIManager;

public class InGameMenu extends Table implements InputProcessor {

    public InGameMenu() {
        Skin skin = UIManager.getInstance().getSkin();
        TextButton returnToGameButton = new TextButton("Return to Game", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton quitToMainMenuButton = new TextButton("Quit to Main Menu", skin);
        TextButton quitToDesktopButton = new TextButton("Quit to Desktop", skin);

        returnToGameButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WinterGame.world.getSystem(HudSystem.class).toggleInGameMenu();
                return true;
            }
        });
        optionsButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                HudSystem hudSystem = WinterGame.world.getSystem(HudSystem.class);
                hudSystem.toggleInGameMenu();
                hudSystem.toggleInGameOptions();
                return true;
            }
        });
        quitToMainMenuButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WinterGame.gameClient.sendLogout();
                WinterGame.getInstance().changeScreen(MenuScreen.class);
                return true;
            }
        });
        quitToDesktopButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WinterGame.gameClient.sendLogout();
                Gdx.app.exit();
                return true;
            }
        });

        setX(0);
        setY(0);
        setWidth(Gdx.graphics.getWidth());
        setHeight(Gdx.graphics.getHeight());

        setFillParent(true);
        add(returnToGameButton).width(300).height(50);
        row();
        add(optionsButton).width(300).height(50);
        row();
        add(quitToMainMenuButton).width(300).height(50);
        row();
        add(quitToDesktopButton).width(300).height(50);
    }

    public boolean keyDown(int i) {
        return false;
    }

    public boolean keyUp(int i) {
        return false;
    }

    public boolean keyTyped(char c) {
        return false;
    }

    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;
    }

    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    public boolean scrolled(int i) {
        return false;
    }
}
