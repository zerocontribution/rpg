package io.zerocontribution.winter;

public enum State {
    STAND("Stand"),
    RUN("Run"),
    SNEAK("Sneak");

    private String text;

    private State(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
