package io.zerocontribution.winter;

public enum State {
    IDLE("Idle"),
    RUN("Run"),
    SNEAK("Sneak"),
    DYING("Dying");

    private String text;

    private State(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
