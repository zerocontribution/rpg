package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import io.zerocontribution.winter.components.*;

public class AnimationUpdatingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<AnimationName> animationNameMapper;

    @Mapper
    ComponentMapper<AnimationTimer> animationTimerMapper;

    @Mapper
    ComponentMapper<Name> nameMapper;

    @Mapper
    ComponentMapper<Condition> conditionMapper;

    @Mapper
    ComponentMapper<Facing> facingMapper;

    @SuppressWarnings("unchecked")
    public AnimationUpdatingSystem() {
        super(Aspect.getAspectForAll(Facing.class, Name.class, Condition.class, AnimationName.class, AnimationTimer.class));
    }

    @Override
    protected void process(Entity entity) {
        AnimationName prevAnimName = animationNameMapper.get(entity);
        AnimationTimer animTimer = animationTimerMapper.get(entity);
        Facing facing = facingMapper.get(entity);
        Name name = nameMapper.get(entity);
        Condition state = conditionMapper.get(entity);
        String currentAnimName = constructAnimationName(name, state, facing);

        if (!currentAnimName.equals(prevAnimName.name)) {
            prevAnimName.name = currentAnimName;
            animTimer.time = 0f;
        } else {
            animTimer.time += world.getDelta();
        }
    }

    private String constructAnimationName(Name name, Condition state, Facing facing) {
        StringBuilder sb = new StringBuilder();

        sb.append(name.name);
        sb.append(state.state);
        sb.append(facing.direction);

        return sb.toString();
    }

}
