package io.zerocontribution.winter.combat.abilities;

import com.artemis.Entity;
import com.artemis.World;
import io.zerocontribution.winter.combat.processors.CombatProcessor;
import io.zerocontribution.winter.components.Position;

public abstract class Ability {

    public int id;
    public String name;

    abstract public Entity create(World world, Entity source, String group, Position position);

    abstract public CombatProcessor getCombatProcessor();
}
