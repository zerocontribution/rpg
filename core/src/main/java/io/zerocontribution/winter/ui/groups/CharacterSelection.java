package io.zerocontribution.winter.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.assets.ClassAsset;
import io.zerocontribution.winter.ui.UIManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterSelection extends Group implements Disposable {

    Map<String, Texture> textureCache = new HashMap<String, Texture>();

    Label selected;

    public CharacterSelection(final List<ClassAsset> gameClasses) {
        setName("characterSelection");
        final Skin skin = UIManager.getInstance().getSkin();
        selected = new Label("", skin);
        selected.setY(30);
        addActor(selected);

        final ImageButton[] buttons = new ImageButton[gameClasses.size()];

        for (int i = 0; i < gameClasses.size(); i++) {
            final ClassAsset gameClass = gameClasses.get(i);
            final int buttonIdx = i;

            Drawable defaultDrawable = getDrawable(gameClass.previewTexturePath);
            Drawable toggledDrawable = getDrawable(gameClass.previewToggledTexturePath);

            final ImageButton selectClassButton = new ImageButton(defaultDrawable, toggledDrawable, toggledDrawable);
            selectClassButton.addListener(new InputListener() {
                Table tooltip;
                int buttonIndex = buttonIdx;
                String selectedClass = null;

                // TODO Font scale: Make dedicated fonts
                // TODO Description needs to have its location fixed
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (tooltip == null) {
                        tooltip = new Table(skin);
                        tooltip.debug();
                        tooltip.align(Align.left);
                        tooltip.defaults().align(Align.left).pad(0).maxHeight(30).expandX().top();
                        tooltip.add(new Label(gameClass.label, skin));

                        Label description = new Label(gameClass.description, skin);
                        description.setWrap(true);
                        description.setFontScale(0.5f);
                        tooltip.row();
                        tooltip.add(description);

                        addActor(tooltip);
                    }
                    selected.setVisible(false);
                    tooltip.setVisible(true);
                }

                public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (tooltip != null) {
                        tooltip.setVisible(false);
                        selected.setVisible(true);
                    }
                }

                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (selectClassButton.isChecked()) {
                        selected.setText("");
                        WinterGame.gameClient.sendPlayerLobbyState(null);
                    } else {
                        for (int n = 0; n < buttons.length; n++) {
                            if (n != buttonIndex) {
                                buttons[n].setChecked(false);
                            }
                        }
                        selected.setText(gameClass.label);
                        WinterGame.gameClient.sendPlayerLobbyState(gameClass.name);
                    }

                    return true;
                }
            });
            buttons[i] = selectClassButton;
            selectClassButton.setX(selectClassButton.getWidth() * i + (i * 10));
            selectClassButton.setY(60);

            addActor(selectClassButton);
        }

        setWidth((buttons.length * (buttons[0].getWidth() + 10)) - 10);
        setHeight(buttons[0].getHeight() + 60);
    }

    public Label getSelected() {
        return selected;
    }

    private Drawable getDrawable(String texturePath) {
        return new TextureRegionDrawable(new TextureRegion(getTexture(texturePath)));
    }

    private Texture getTexture(String texturePath) {
        if (!textureCache.containsKey(texturePath)) {
            textureCache.put(texturePath, new Texture(Gdx.files.internal(texturePath)));
        }
        return textureCache.get(texturePath);
    }

    public void dispose() {
        for (String key : textureCache.keySet()) {
            textureCache.get(key).dispose();
        }
    }
}
