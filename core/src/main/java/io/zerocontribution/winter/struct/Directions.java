package io.zerocontribution.winter.struct;

public enum Directions {
    UP("Up"),
    UP_LEFT("UpLeft"),
    UP_RIGHT("UpRight"),
    DOWN("Down"),
    DOWN_LEFT("DownLeft"),
    DOWN_RIGHT("DownRight"),
    RIGHT("Right"),
    LEFT("Left");

    private String text;

    private Directions(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
