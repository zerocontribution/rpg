package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.components.*;

public class AnimationRenderingSystem extends EntitySystem {

    @Mapper
    ComponentMapper<AnimationName> animationMapper;

    @Mapper
    ComponentMapper<AnimationTimer> timerMapper;

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<Dimensions> dimensionsMapper;

    @Mapper
    ComponentMapper<SpriteColor> spriteColorMapper;

    private SpriteBatch spriteBatch;

    @SuppressWarnings("unchecked")
    public AnimationRenderingSystem(SpriteBatch spriteBatch) {
        super(Aspect.getAspectForAll(Condition.class, AnimationName.class, AnimationTimer.class, Position.class, Dimensions.class));
        this.spriteBatch = spriteBatch;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    private void process(Entity e) {
        AnimationName name = animationMapper.get(e);
        AnimationTimer timer = timerMapper.get(e);
        Position position = positionMapper.get(e);
        Dimensions dimensions = dimensionsMapper.get(e);

        if (spriteColorMapper.has(e)) {
            SpriteColor spriteColor = spriteColorMapper.get(e);
            spriteBatch.setColor(spriteColor.r, spriteColor.g, spriteColor.b, spriteColor.a);
        }

        spriteBatch.draw(Assets.getAnimationFrame(name.name, timer.time), position.x, position.y, dimensions.width, dimensions.height);

        if (spriteColorMapper.has(e)) {
            // Reset back to the original values.
            spriteBatch.setColor(1, 1, 1, 1);
        }
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        spriteBatch.begin();
        for (int i = 0; i < entities.size(); i++) {
            process(entities.get(i));
        }
        spriteBatch.end();
    }

}
