package io.zerocontribution.winter.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.combat.abilities.PunchAbility;
import io.zerocontribution.winter.ui.UIManager;

import java.util.ArrayList;
import java.util.List;

public class ActionBar extends Actor implements InputProcessor {

    final static int CELL_SIZE = 48;
    final static int CELL_PADDING = 2;
    final static int CELL_TOTAL_SIZE = 50;

    // TODO Should be configurable.
    List<Integer> keys = new ArrayList<Integer>();

    int width = (Gdx.graphics.getWidth() - (CELL_TOTAL_SIZE * 5)) / 2;

    public ActionBar() {
        keys.add(Input.Keys.NUM_1);
        keys.add(Input.Keys.NUM_2);
        keys.add(Input.Keys.NUM_3);
        keys.add(Input.Keys.NUM_4);
        keys.add(Input.Keys.NUM_5);

        setPosition(width, 10);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        BitmapFont font = UIManager.getInstance().getSkin().getFont("default-font");
        Texture slot = new Texture(Gdx.files.internal("gfx/slot.png"));

        float x = getX();
        for (int i = 0; i < keys.size(); i++) {
            // TODO: Class system & getting abilities
            Ability ability = (i == 0) ? Assets.abilities.get(1) : null;

            if (ability != null) {
                batch.draw(ability.getTexture(), x, getY(), CELL_SIZE, CELL_SIZE);
            } else {
                batch.draw(slot, x, getY(), CELL_SIZE, CELL_SIZE);
            }
            font.draw(batch, String.valueOf(i + 1), width + (CELL_TOTAL_SIZE * i) + CELL_PADDING, CELL_TOTAL_SIZE);
            x += CELL_TOTAL_SIZE;
        }
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
