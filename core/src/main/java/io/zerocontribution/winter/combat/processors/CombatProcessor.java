package io.zerocontribution.winter.combat.processors;

import com.artemis.Entity;
import com.artemis.World;

public interface CombatProcessor {

    public void initialize(World world);
    public void process(Entity ability);
    public boolean collision(Entity ability, Entity source, Entity target);
    public void handle(Entity ability, Entity source, Entity target);
    public void delete();

}
