package io.zerocontribution.winter.struct;

import com.badlogic.gdx.utils.Pool;

public class Pair {
    public int x, y;

    private static Pair tempPair;

    private static Pool<Pair> pairPool = new Pool<Pair>() {
        @Override
        protected Pair newObject() {
            return new Pair(0, 0);
        }
    };

    public static Pair get(float x, float y) {
        tempPair = pairPool.obtain();
        tempPair.x = (int) x;
        tempPair.y = (int) y;
        return tempPair;
    }

    public static void free(Pair pair) {
        pairPool.free(pair);
    }

    private Pair(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public String toLog() {
        return new StringBuilder()
                .append("Pair[")
                .append(x).append(",").append(y)
                .append("]")
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (!(other instanceof Pair)) {
            return false;
        }

        Pair otherPair = (Pair) other;
        return otherPair.x == x && otherPair.y == y;
    }

    @Override
    public int hashCode() {
        return x * 5 ^ y * 7;
    }

}
