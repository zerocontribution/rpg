package io.zerocontribution.winter.event;

public interface GameEvent<L> {
    public void notify(final L listener);
}
