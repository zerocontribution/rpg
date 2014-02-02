package io.zerocontribution.winter.systems;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.hud.ActionBar;
import io.zerocontribution.winter.hud.InventoryFrame;

public class HudSystem extends VoidEntitySystem {

    public Stage stage;
    public Skin skin;

    ActionBar actionBar;
    InventoryFrame inventoryFrame;

    InputMultiplexer input;

    @SuppressWarnings("unchecked")
    public HudSystem(InputMultiplexer input) {
        this.input = input;
        this.stage = new Stage();
        this.skin = createSkin();
    }

    public void toggleInventory() {
        if (!inventoryFrame.hasParent()) {
            stage.addActor(inventoryFrame);
            input.addProcessor(inventoryFrame);
        } else {
            inventoryFrame.remove();
            input.removeProcessor(inventoryFrame);
        }
    }

    public Entity getOwner() {
        return world.getManager(TagManager.class).getEntity(Constants.Tags.PLAYER);
    }

    @Override
    protected void initialize() {
        this.actionBar = new ActionBar(world, this);
        this.inventoryFrame = new InventoryFrame(world, this);

        input.addProcessor(actionBar);
    }

    @Override
    protected void processSystem() {
        stage.act(world.getDelta());
        stage.draw();
    }

    protected Skin createSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        skin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyle.focusedBackground = skin.newDrawable("white", Color.LIGHT_GRAY);
        textFieldStyle.disabledBackground = skin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyle.cursor = skin.newDrawable("white", Color.BLUE);
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.BLUE;
        textFieldStyle.disabledFontColor = Color.LIGHT_GRAY;
        textFieldStyle.messageFont = skin.getFont("default");
        textFieldStyle.messageFontColor = Color.CYAN;
        textFieldStyle.selection = skin.newDrawable("white", Color.CYAN);
        skin.add("default", textFieldStyle);

        return skin;
    }

}
