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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.Cam;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.components.Sprite;
import io.zerocontribution.winter.utils.GdxLogHelper;

import java.util.*;

public class SpriteRenderSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<Sprite> sm;

    @Mapper
    ComponentMapper<Cam> cm;

    private HashMap<String, TextureAtlas.AtlasRegion> regions;
    private TextureAtlas textureAtlas;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Bag<TextureAtlas.AtlasRegion> regionsByEntity;
    private List<Entity> sortedEntities;

    @SuppressWarnings("unchecked")
    public SpriteRenderSystem() {
        super(Aspect.getAspectForAll(Position.class, Sprite.class));
    }

    // TODO Need to include an asset manager in the mix here... don't want to include all assets in a single atlas.
    @Override
    protected void initialize() {
        regions = new HashMap<String, TextureAtlas.AtlasRegion>();
        textureAtlas = new TextureAtlas(Gdx.files.internal("img/player/player.atlas"), Gdx.files.internal("img/player"));
        for (TextureAtlas.AtlasRegion r : textureAtlas.getRegions()) {
            regions.put(r.name, r);
        }
        regionsByEntity = new Bag<TextureAtlas.AtlasRegion>();

        spriteBatch = new SpriteBatch();

        sortedEntities = new ArrayList<Entity>();

        Texture fontTexture = new Texture(Gdx.files.internal("fonts/normal_0.png"));
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.MipMapLinearLinear);
        TextureRegion fontRegion = new TextureRegion(fontTexture);
        font = new BitmapFont(Gdx.files.internal("fonts/normal.fnt"), fontRegion, false);
        font.setUseIntegerPositions(false);

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
            Sprite sprite = sm.get(e);

            TextureAtlas.AtlasRegion spriteRegion = regionsByEntity.get(e.getId());
            spriteBatch.setColor(sprite.r, sprite.g, sprite.b, sprite.a);

            float posX = position.x - spriteRegion.getRegionWidth() / 2 * sprite.scaleX;
            float posY = position.y - spriteRegion.getRegionHeight() / 2 * sprite.scaleY;

            spriteBatch.draw(spriteRegion, posX, posY, 0, 0, spriteRegion.getRegionWidth(), spriteRegion.getRegionHeight(), sprite.scaleX, sprite.scaleY, sprite.rotation);
        }
    }

    @Override
    protected void end() {
        spriteBatch.end();
    }

    @Override
    protected void inserted(Entity e) {
        Sprite sprite = sm.get(e);
        regionsByEntity.set(e.getId(), regions.get(sprite.name));

        sortedEntities.add(e);

        Collections.sort(sortedEntities, new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                Sprite s1 = sm.get(o1);
                Sprite s2 = sm.get(o2);
                return s1.layer.compareTo(s2.layer);
            }
        });
    }

    @Override
    protected void removed(Entity e) {
        regionsByEntity.set(e.getId(), null);
        sortedEntities.remove(e);
    }

}
