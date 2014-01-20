package io.zerocontribution.winter.screens;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.systems.*;
import io.zerocontribution.winter.systems.client.ClientNetworkSystem;
import io.zerocontribution.winter.utils.ClientGlobals;

/**
 * Responsible for loading the actual game's dependencies; such as assets for the level.
 *
 * @todo We may not want this after all... world should be setup from the start.
 */
public class GameLoadScreen extends AbstractScreen {

    SpriteBatch screenBatch;
    BitmapFont font;
    String map;

    public GameLoadScreen(WinterGame game, String map) {
        super(game);
        screenBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        this.map = map;
    }

    @Override
    public void show() {
        super.show();
        screenBatch.begin();
        font.draw(screenBatch, "Loading...", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        screenBatch.end();

        loadGame();
    }

    public void loadGame() {
        Assets.loadConfigurations();
        Assets.loadMap(map);
        Assets.loadImages();

        SpriteBatch spriteBatch = new SpriteBatch();

        World world = new World();
        world.setManager(new GroupManager());
        world.setManager(new TagManager());

        world.setSystem(new ClientNetworkSystem(game.gameClient.client, 33));

        if (Constants.DEBUG) {
            world.setSystem(new FPSLoggingSystem());
        }

        // TODO Refactor these systems to not change entity states (e.g. setting State to DYING during combat)
        world.setSystem(new CameraSystem());
        world.setSystem(new PlayerInputSystem());
        world.setSystem(new AIProcessingSystem());
        world.setSystem(new ActionProcessingSystem());
        world.setSystem(new CombatProcessingSystem());
        world.setSystem(new DamageProcessingSystem());
        world.setSystem(new CollisionSystem());
        world.setSystem(new MovementSystem());
        world.setSystem(new AnimationUpdatingSystem());
        world.setSystem(new ExpiredProcessingSystem());

        world.setSystem(new MapRenderingSystem(), true);
        world.setSystem(new AnimationRenderingSystem(spriteBatch), true);

        if (Constants.DEBUG) {
            world.setSystem(new CollisionDebugSystem(), true);
            world.setSystem(new DebugHudSystem(), true);
        }

        EntityFactory.createMap(world, spriteBatch).addToWorld();

        world.initialize();

        game.world = world;
    }

    @Override
    public void dispose() {
        super.dispose();
        screenBatch.dispose();
        font.dispose();
    }

}
