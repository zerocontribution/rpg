package io.zerocontribution.winter.components;

import com.artemis.Entity;

// TODO: This is supposed to be an EntityComponent, but I want to fix movement packets first.
public class Damage extends BaseComponent {
    public Entity ability;
    public Entity source;
    public int healthAmount;
}
