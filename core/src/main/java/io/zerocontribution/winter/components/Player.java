package io.zerocontribution.winter.components;

public class Player extends EntityComponent {
    public String name;
    public String group = "player1"; // TODO identifier for the ECS to group player actions, etc.

    public Player() {}
    public Player(String name) {
        this.name = name;
    }
}
