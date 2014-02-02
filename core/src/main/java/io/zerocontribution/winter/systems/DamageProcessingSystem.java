package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.EntityFactory;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.components.Condition;
import io.zerocontribution.winter.components.Damage;
import io.zerocontribution.winter.components.GridPosition;
import io.zerocontribution.winter.components.Stats;
import io.zerocontribution.winter.utils.GdxLogHelper;

public class DamageProcessingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Damage> damageMapper;

    @Mapper
    ComponentMapper<Stats> statsMapper;

    @Mapper
    ComponentMapper<Condition> conditionMapper;

    @Mapper
    ComponentMapper<GridPosition> gridPositionMapper;

    @SuppressWarnings("unchecked")
    public DamageProcessingSystem() {
        super(Aspect.getAspectForAll(Damage.class));
    }

    @Override
    protected void process(Entity e) {
        Damage damage = damageMapper.get(e);
        Stats targetStats = statsMapper.get(e);

        // TODO Clamp between 0 and maxHealth

        if (targetStats.health > 0) {
            targetStats.health -= damage.healthAmount;
            GdxLogHelper.log("damage-processor", damage.source + " hits " + e + " for " + damage.healthAmount);

            if (targetStats.health <= 0) {
                Stats sourceStats = statsMapper.get(damage.source);

                // TODO: Need animations for dying so this doesn't exception out during rendering.
                Condition condition = conditionMapper.get(e);
                condition.state = State.DYING;

                GdxLogHelper.log("damage-processor", damage.source + " killed " + e);
                creditKill(damage.source, sourceStats, targetStats);

                GridPosition gridPosition = gridPositionMapper.getSafe(e);
                if (gridPosition != null) {
                    createRandomDrop(gridPosition.x - 1, gridPosition.y - 1);
                }
            }
        }

        e.removeComponent(Damage.class);
        e.changedInWorld();
    }

    private void creditKill(Entity source, Stats sourceStats, Stats targetStats) {
        sourceStats.kills++;

        double xpGain = Math.ceil((sourceStats.level - targetStats.level + 1) * 300);
        GdxLogHelper.log("damage-processing", source.toString() + " gained " + xpGain + " xp");
        sourceStats.experience += xpGain;

        if (sourceStats.experience >= sourceStats.maxExperience && sourceStats.maxLevel > sourceStats.level) {
            sourceStats.level++;
            sourceStats.experience = sourceStats.experience - sourceStats.maxExperience;
            sourceStats.maxExperience *= 0.1;

            sourceStats.technicalAbility += 1;
            sourceStats.cool += 1;
            sourceStats.attractiveness += 1;
            sourceStats.body += 1;

            sourceStats.maxHealth *= 0.1;
            sourceStats.maxPower *= 0.1;

            sourceStats.health = sourceStats.maxHealth;
            sourceStats.power = sourceStats.maxPower;

            GdxLogHelper.log("damage-processing", source.toString() + " leveled up to " + sourceStats.level);
        }
    }

    private void createRandomDrop(float x, float y) {
        GdxLogHelper.log("damage-processing", "dropping loot");

        int itemId = MathUtils.random(0, Assets.items.size());
        int credits = MathUtils.random(10, 300);

        EntityFactory.createDrop(world, credits, itemId, x, y).addToWorld();
    }

}
