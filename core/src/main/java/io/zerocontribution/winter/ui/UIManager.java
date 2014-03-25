package io.zerocontribution.winter.ui;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.WinterGame;

public class UIManager {

    final public static String DEFAULT = "default";

    private static UIManager instance = new UIManager();

    private Skin skin;
    private Stage stage;
    private BitmapFont font;
    private InputMultiplexer inputMultiplexer;
    private Entity localPlayer;

    public static UIManager getInstance() {
        return instance;
    }

    private UIManager() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Should be used strictly for debuggers and that sort of thing.
        font = new BitmapFont();
        font.setUseIntegerPositions(false);

        stage = new Stage();
        skin = new Skin(Gdx.files.internal("assets/ui/HoloSkin/Holo-dark-hdpi.json"));

        localPlayer = WinterGame.world.getManager(TagManager.class).getEntity(Constants.Tags.LOCAL_PLAYER);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }

    public BitmapFont getFont() {
        return font;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
        this.inputMultiplexer = inputMultiplexer;
    }

    public Entity getLocalPlayer() {
        return localPlayer;
    }
}
