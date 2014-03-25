package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import io.zerocontribution.winter.State;
import io.zerocontribution.winter.combat.Abilities;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;

// TODO: Refactor to ServerActionProcessingSystem
// TODO: ClientActionProcessingSystem should only process local player.
public class ActionProcessingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<ActionInput> actionInputMapper;

    @Mapper
    ComponentMapper<Actor> actorMapper;

    @Mapper
    ComponentMapper<Condition> conditionMapper;

    @Mapper
    ComponentMapper<Position> positionMapper;

    @SuppressWarnings("unchecked")
    public ActionProcessingSystem() {
        super(Aspect.getAspectForAll(Actor.class, ActionInput.class));
    }

    @Override
    protected void process(Entity e) {
        if (conditionMapper.get(e).state != State.DYING) {
            ActionInput action = actionInputMapper.get(e);
            Actor actor = actorMapper.get(e);

            if (actor.hasAbility(action.abilityId)) {
                if (!actor.isAbilityInCooldown(action.abilityId)) {
                    actor.resetAbilityCooldown(action.abilityId);

                    GdxLogHelper.log("action-input", "Executing action " + action.abilityId);
                    Ability ability = Abilities.get(action.abilityId);

                    Entity abilityEntity = ability.create(world, e, positionMapper.get(e));

                    e.addComponent(new CombatAction(abilityEntity, ability.getCombatProcessor(), action.target));
                } else {
                    GdxLogHelper.log("action-input", "Action in cooldown");
                }
            } else {
                // TODO Oh shit, son. Cheat detection.
                GdxLogHelper.log("action-input", "Actor does not have ability: " + action.abilityId);
            }
        } else {
            GdxLogHelper.log("action-input", "Actor is dead, cancelling action");
        }

        e.removeComponent(ActionInput.class);
        e.changedInWorld();
    }

}
