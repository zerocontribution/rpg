package io.zerocontribution.winter.hud;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.zerocontribution.winter.components.Inventory;
import io.zerocontribution.winter.systems.HudSystem;
import io.zerocontribution.winter.utils.GdxLogHelper;

/**
 * @todo This inventory frame only supports 32 item inventories right now.
 */
public class InventoryFrame extends Actor implements InputProcessor {

    World world;
    HudSystem hudSystem;
    Texture slot;
    Inventory inventory;

    int width, height;

    public InventoryFrame(World world, HudSystem hudSystem) {
        this.world = world;
        this.hudSystem = hudSystem;

        inventory = world.getMapper(Inventory.class).get(hudSystem.getOwner());

        slot = new Texture(Gdx.files.internal("gfx/slot.png"));
        width = 600;
        height = 300;

        setPosition(10, 150);
    }

    public void draw(SpriteBatch batch, float percentAlpha) {
        float posX = getX() + 400;
        float posY = getY();

        BitmapFont font = hudSystem.skin.getFont("default");
        font.draw(batch, "Inventory", 60, 360);

        Inventory.ItemSlot[] slots = inventory.getSlots();
        for (int i = slots.length; i > 0; i--) {
            batch.draw(slot, posX, posY, 48, 48);

            posX -= 50;
            if ((i - 1) % 8 == 0) {
                posY += 50;
                posX = getX() + 400;
            }
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
