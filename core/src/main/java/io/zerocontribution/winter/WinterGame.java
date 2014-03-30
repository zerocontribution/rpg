package io.zerocontribution.winter;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import io.zerocontribution.winter.client.GameClient;
import io.zerocontribution.winter.client.screens.MenuScreen;
import io.zerocontribution.winter.event.Events;
import io.zerocontribution.winter.server.GameServer;
import io.zerocontribution.winter.utils.GdxLogHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @todo ClientGlobals stuff should also live in here; I think.
 */
public class WinterGame extends Game {

    private static WinterGame instance;

    public static World world;

    public static GameClient gameClient;

    public static Events events = new Events();

    private Class pendingScreen;
    private List<Object> pendingScreenArgs;

    private GameServer gameServer;

    public static WinterGame getInstance() {
        return instance;
    }

    @Override
    public void create () {
        instance = this;

        initializeClient();

        Assets.loadConfigurations();
        setScreen(new MenuScreen());
    }

    private void initializeClient() {
        gameClient = new GameClient();

        world = new World();
        world.setManager(new GroupManager());
        world.setManager(new TagManager());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render () {
        super.render();
        // TODO Add world reset
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            try {
                setScreen(getScreen().getClass().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Oh, sweet baby jesus
        if (pendingScreen != null) {
            GdxLogHelper.log("WinterGame", "Received request to change screen: " + pendingScreen.getSimpleName());

            List<Object> screenArgs = new ArrayList<Object>();
            screenArgs.add(this);
            screenArgs.addAll(pendingScreenArgs);

            Class[] argTypes = new Class[screenArgs.size()];
            for (int i = 0; i < screenArgs.size(); i++) {
                argTypes[i] = screenArgs.get(i).getClass();
            }

            Screen newScreen = null;
            try {
                newScreen = (Screen) pendingScreen.getConstructor(argTypes).newInstance(screenArgs.toArray());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (newScreen != null) {
                setScreen(newScreen);
            }

            pendingScreen = null;
            pendingScreenArgs = null;
        }
    }

    @Override
    public void dispose() {
        stopServer();
    }

    public void changeScreen(Class screenClass) {
        changeScreen(screenClass, new ArrayList<Object>());
    }

    public void changeScreen(Class screenClass, List<Object> args) {
        this.pendingScreen = screenClass;
        this.pendingScreenArgs = args;
    }

    public boolean isHost() {
        return gameServer != null;
    }

    public void startServer() {
        gameServer = new GameServer();
    }

    public void stopServer() {
        if (isHost()) {
            gameServer.stop();
        }
    }

}
