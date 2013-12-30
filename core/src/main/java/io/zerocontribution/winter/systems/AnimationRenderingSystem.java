package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;

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
    ComponentMapper<Cam> cameraMapper;

    private SpriteBatch batch;

    private OrthographicCamera camera;

    @SuppressWarnings("unchecked")
    public AnimationRenderingSystem(SpriteBatch batch) {
        super(Aspect.getAspectForAll(Condition.class, AnimationName.class, AnimationTimer.class, Position.class, Dimensions.class));
        this.batch = batch;
    }

    @Override
    protected void initialize() {
        camera = cameraMapper.get(world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW)).camera;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    private void process(Entity entity) {
        AnimationName name = animationMapper.get(entity);
        AnimationTimer timer = timerMapper.get(entity);
        Position position = positionMapper.get(entity);
        Dimensions dimensions = dimensionsMapper.get(entity);

        batch.draw(Assets.getAnimationFrame(name.name, timer.time), position.x, position.y, dimensions.width, dimensions.height);
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        batch.begin();
        for (int i = 0; i < entities.size(); i++) {
            process(entities.get(i));
        }
        batch.end();
    }

}
