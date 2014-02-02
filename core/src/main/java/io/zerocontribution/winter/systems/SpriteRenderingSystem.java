package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.components.Dimensions;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.components.Sprite;
import io.zerocontribution.winter.components.SpriteColor;

public class SpriteRenderingSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Sprite> spriteMapper;

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<Dimensions> dimensionsMapper;

    @Mapper
    ComponentMapper<SpriteColor> spriteColorMapper;

    private SpriteBatch spriteBatch;

    @SuppressWarnings("unchecked")
    public SpriteRenderingSystem(SpriteBatch spriteBatch) {
        super(Aspect.getAspectForAll(Sprite.class, Position.class, Dimensions.class));
        this.spriteBatch = spriteBatch;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        spriteBatch.begin();
        for (int i = 0; i < entities.size(); i++) {
            process(entities.get(i));
        }
        spriteBatch.end();
    }

    private void process(Entity e) {
        Sprite sprite = spriteMapper.get(e);
        Position position = positionMapper.get(e);
        Dimensions dimensions = dimensionsMapper.get(e);
        SpriteColor spriteColor = spriteColorMapper.getSafe(e);

        if (spriteColor != null) {
            spriteBatch.setColor(spriteColor.r, spriteColor.g, spriteColor.b, spriteColor.a);
        }

        spriteBatch.draw(sprite.texture, position.x, position.y, dimensions.width, dimensions.height);

        if (spriteColor != null) {
            spriteBatch.setColor(1, 1, 1, 1);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

}
