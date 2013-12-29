package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.AnimationSprite;
import io.zerocontribution.winter.components.Cam;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.components.Velocity;

import java.util.*;

public class AnimationRenderSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<Velocity> vm;

    @Mapper
    ComponentMapper<AnimationSprite> sm;

    @Mapper
    ComponentMapper<Cam> cm;

    private HashMap<String, TextureAtlas.AtlasRegion> regions;
    private TextureAtlas textureAtlas;
    private TextureRegion texture;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;

    private float stateTime;

    private Bag<TextureAtlas.AtlasRegion> regionsByEntity;
    private List<Entity> sortedEntities;

    @SuppressWarnings("unchecked")
    public AnimationRenderSystem() {
        super(Aspect.getAspectForAll(Position.class, AnimationSprite.class));
    }

    // TODO Change the location of the atlas
    // TODO AssetManager
    @Override
    protected void initialize() {
        regions = new HashMap<String, TextureAtlas.AtlasRegion>();
        textureAtlas = new TextureAtlas("img/player/player.atlas");
        for (TextureAtlas.AtlasRegion r : textureAtlas.getRegions()) {
            regions.put(r.name, r);
        }
        regionsByEntity = new Bag<TextureAtlas.AtlasRegion>();

        spriteBatch = new SpriteBatch();

        sortedEntities = new ArrayList<Entity>();

        camera = cm.get(world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW)).camera;
    }

    @Override
    protected void begin() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        for (int i = 0; sortedEntities.size() > i; i++) {
            process(sortedEntities.get(i));
        }
    }

    protected void process(Entity e) {
        if (pm.has(e)) {
            Position position = pm.getSafe(e);
            AnimationSprite sprite = sm.get(e);

            TextureAtlas.AtlasRegion spriteRegion = regionsByEntity.get(e.getId());
            spriteBatch.setColor(sprite.r, sprite.g, sprite.b, sprite.a);

            float posX, posY;
            if (vm.has(e)) {
                // If we have velocity, the entity has already been moved by the MovementSystem and we don't need to do
                // anything about it.
                posX = position.x;
                posY = position.y;
            } else {
                posX = position.x * (spriteRegion.getRegionWidth() / 2 * sprite.scaleX);
                posY = position.y * (spriteRegion.getRegionHeight() / 2 * sprite.scaleY);
            }

            sprite.time += Gdx.graphics.getDeltaTime(); // TODO Feel like this should be right before getting the keyframe.
            if (sprite.active) {
                texture = sprite.animation.getKeyFrame(sprite.time, sprite.loop);
            } else {
                texture = sprite.animation.getKeyFrame(0, sprite.loop);
            }

            spriteBatch.draw(spriteRegion, posX, posY, 0, 0, spriteRegion.getRegionWidth(), spriteRegion.getRegionHeight(), sprite.scaleX, sprite.scaleY, sprite.rotation);
        }
    }

    protected void end() {
        spriteBatch.end();
    }

    @Override
    protected void inserted(Entity e) {
        AnimationSprite sprite = sm.get(e);
        regionsByEntity.set(e.getId(), regions.get(sprite.name));

        TextureAtlas.AtlasRegion texture = regionsByEntity.get(e.getId());//textureAtlas.findRegion(sprite.name)
        sprite.animation = new Animation(sprite.frameTime, texture);

        sortedEntities.add(e);

        Collections.sort(sortedEntities, new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                AnimationSprite s1 = sm.get(o1);
                AnimationSprite s2 = sm.get(o2);
                return s1.layer.compareTo(s2.layer);
            }
        });
    }

    public void addTime(float t) {
        stateTime += t;
    }

    @Override
    protected void removed(Entity e) {
        regionsByEntity.set(e.getId(), null);
        sortedEntities.remove(e);
    }

}
