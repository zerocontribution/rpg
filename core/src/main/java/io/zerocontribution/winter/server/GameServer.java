package io.zerocontribution.winter.server;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.pevnostgames.lwjglserver.ServerFiles;

/**
 * The main GameServer class. It's derived from {@code net.pevnostgames.lwjglserver}, but doesn't implement the libGDX
 * Application interface. Since the server is run from the same process; we can't override the Gdx static variables. We
 * must maintain everything on our own.
 *
 * @todo Need a way to postpone world creation until the host has launched the game. The run loop should be pretty dumb
 *       for awhile until then; using a conditional to actually run game code vs. lobby code. Maybe lobby code is its
 *       own world that gets canned when the game is launched?
 * @todo Manage lobbies & global state via a runnable?
 */
public class GameServer implements Runnable {

    protected final ServerFiles files;
    protected final Array<Runnable> runnables = new Array<Runnable>();
    protected final Array<Runnable> executedRunnables = new Array<Runnable>();
    protected final Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();
    protected Thread mainLoopThread;
    protected boolean running = true;

    protected long lastTime = 0;
    protected float deltaTime = 0;
    protected float targetDelta = 0;

    private Screen screen;

    public GameServer() {
        files = new ServerFiles();
        targetDelta = 1.0f / 60;
        initialize();
    }

    public void run() {
        mainLoop();
    }

    private void initialize() {
        mainLoopThread = new Thread("GameServer") {
            public void run() {
                try {
                    GameServer.this.mainLoop();
                } catch (Throwable t) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else {
                        throw new GdxRuntimeException(t);
                    }
                }
            }
        };
        mainLoopThread.start();
    }

    private void mainLoop() {
        create();

        lastTime = System.nanoTime();

        while (running) {
            boolean shouldRender = (System.nanoTime() - lastTime) / 1000000000.0f >= targetDelta;

            synchronized (runnables) {
                executedRunnables.clear();
                executedRunnables.addAll(runnables);
                runnables.clear();
            }

            for (int i = 0; i < executedRunnables.size; i++) {
                shouldRender = true;
                executedRunnables.get(i).run();
            }

            if (!running) break;

            if (shouldRender) {
                long time = System.nanoTime();
                deltaTime = (time - lastTime) / 1000000000.0f;
                lastTime = time;
                render();
            }
        }

        Array<LifecycleListener> listeners = lifecycleListeners;
        synchronized (listeners) {
            for (LifecycleListener listener : listeners) {
                listener.pause();
                listener.dispose();
            }
        }
        pause();
        dispose();
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public Files getFiles() {
        return files;
    }

    public void postRunnable(Runnable runnable) {
        synchronized (runnables) {
            runnables.add(runnable);
        }
    }

    public void exit() {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                running = false;
            }
        });
    }

    public void addLifecycleListener(LifecycleListener listener) {
        synchronized (lifecycleListeners) {
            lifecycleListeners.add(listener);
        }
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        synchronized (lifecycleListeners) {
            lifecycleListeners.removeValue(listener, true);
        }
    }

    public void stop() {
        running = false;
        try {
            mainLoopThread.join();
        } catch (Exception ex) {
        }
    }

    public void create() {
        // TODO Remove screen stuff and move to application listeners instead.
        setScreen(new ServerGameScreen(this));
    }

    public void resize(int i, int i2) {
        if (screen != null) screen.resize(i, i2);
    }

    public void render() {
        if (screen != null) screen.render(getDeltaTime());
    }

    public void pause() {
        if (screen != null) screen.pause();
    }

    public void resume() {
        if (screen != null) screen.resume();
    }

    public void dispose() {
        if (screen != null) screen.dispose();
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
        }
    }

    public Screen getScreen() {
        return screen;
    }

}
