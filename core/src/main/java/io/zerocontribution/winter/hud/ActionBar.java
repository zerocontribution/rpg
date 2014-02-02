package io.zerocontribution.winter.hud;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.components.Player;
import io.zerocontribution.winter.systems.HudSystem;

import java.util.ArrayList;
import java.util.List;

public class ActionBar extends Actor implements InputProcessor {

    static List<Integer> keys = new ArrayList<Integer>();

    World world;
    HudSystem hudSystem;
    BitmapFont font;
    Player player;

    int width = (Gdx.graphics.getWidth() - (50 * 5)) / 2;

    public ActionBar(World world, HudSystem hudSystem) {
        this.hudSystem = hudSystem;
        this.hudSystem.stage.addActor(this);

        player = world.getMapper(Player.class).get(hudSystem.getOwner());

        keys.add(Input.Keys.NUM_1);
        keys.add(Input.Keys.NUM_2);
        keys.add(Input.Keys.NUM_3);
        keys.add(Input.Keys.NUM_4);
        keys.add(Input.Keys.NUM_5);

        setPosition(width, 10);
    }

    @Override
    public void draw(SpriteBatch batch, float percentAlpha) {
        float posX = getX();
        float posY = getY();

        BitmapFont font = hudSystem.skin.getFont("default");
        Texture slot = new Texture(Gdx.files.internal("gfx/slot.png"));
        for (int i = 0; i < keys.size(); i++) {
            Ability ability = player.getAbilityByKey(keys.get(i));
            if (ability != null) {
                batch.draw(ability.getTexture(), posX, posY, 48, 48);
            } else {
                batch.draw(slot, posX, posY, 48, 48);
            }
            font.draw(batch, String.valueOf(i + 1), width + (50 * i) + 2, 50);
            posX += 50;
        }
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
