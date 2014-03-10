package io.zerocontribution.winter.network;

public class ActionCommand implements Command {
    public Lifecycle lifecycle;
    public Action action;

    public static ActionCommand start(Action action) {
        return new ActionCommand(Lifecycle.START, action);
    }

    public static ActionCommand stop(Action action) {
        return new ActionCommand(Lifecycle.STOP, action);
    }

    public ActionCommand() {}
    public ActionCommand(Lifecycle lifecycle, Action action) {
        this.lifecycle = lifecycle;
        this.action = action;
    }

    public enum Lifecycle {
        START,
        STOP
    }

    public enum Action {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_RIGHT,
        MOVE_LEFT
    }
}
