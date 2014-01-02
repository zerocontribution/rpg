package io.zerocontribution.winter;

import java.util.concurrent.TimeUnit;

public class Delay {
    final public long duration;

    public long start;
    public long expiration;

    public Delay(long durationSeconds) {
        duration = durationSeconds;
        reset();
    }

    public boolean isElapsed() {
        return expiration >= System.currentTimeMillis();
    }

    public void reset() {
        start = System.currentTimeMillis();
        expiration = start + TimeUnit.SECONDS.toMillis(duration);
    }

}
