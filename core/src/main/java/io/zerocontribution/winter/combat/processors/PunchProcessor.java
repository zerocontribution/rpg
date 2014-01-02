package io.zerocontribution.winter.combat.processors;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import io.zerocontribution.winter.components.CombatAction;
import io.zerocontribution.winter.components.Damage;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.components.Stats;
import io.zerocontribution.winter.utils.GdxLogHelper;

// TODO If I introduce a reset method in here, then I can share processors across all entities and combat actions
//      That would also mean I could skip initialization most of the time.
public class PunchProcessor implements CombatProcessor {

    ComponentMapper<Position> positionMapper;

    ComponentMapper<Stats> statsMapper;

    ComponentMapper<CombatAction> combatActionMapper;

    @Override
    public void initialize(World world) {
        positionMapper = world.getMapper(Position.class);
        statsMapper = world.getMapper(Stats.class);
        combatActionMapper = world.getMapper(CombatAction.class);
    }

    @Override
    public void process(Entity source) {
        CombatAction combatAction = combatActionMapper.get(source);
        if (collision(combatAction.ability, source, combatAction.target)) {
            handle(combatAction.ability, source, combatAction.target);
        } else {
            GdxLogHelper.log("punch-processor", "Target not within range");
        }

        // TODO Would be good to handle this automatically. Perhaps process returns a boolean?
        source.removeComponent(CombatAction.class);
        source.changedInWorld();

    }

    // TODO A lot of opportunity here for abstraction: Add a range to attack and everything is pre-calculated for most.
    @Override
    public boolean collision(Entity ability, Entity source, Entity target) {
        Position sourcePosition = positionMapper.get(source);

        if (target != null) {
            Position targetPosition = positionMapper.get(target);

            float dst = (float) Math.hypot(
                    Math.abs(sourcePosition.x - targetPosition.x),
                    Math.abs(sourcePosition.y - targetPosition.y)
            );

            GdxLogHelper.log("punch-processor", "range " + dst);

            return dst <= 50;
        }

        return false;
    }

    @Override
    public void handle(Entity ability, Entity source, Entity target) {
//        Stats sourceStats = statsMapper.get(source);
//        Stats targetStats = statsMapper.get(target);

        GdxLogHelper.log("punch-processor", source.toString() + " punching " + target.toString());

        Damage damage = new Damage();
        damage.ability = ability;
        damage.source = source;
        damage.healthAmount = 30;

        target.addComponent(damage);
        target.changedInWorld();
    }

    @Override
    public void delete() {

    }

}
