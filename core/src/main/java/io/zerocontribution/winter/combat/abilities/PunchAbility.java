package io.zerocontribution.winter.combat.abilities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.zerocontribution.winter.Directions;
import io.zerocontribution.winter.combat.processors.CombatProcessor;
import io.zerocontribution.winter.combat.processors.PunchProcessor;
import io.zerocontribution.winter.components.Expiring;
import io.zerocontribution.winter.components.Facing;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.utils.GdxLogHelper;

public class PunchAbility extends Ability {
    static Texture texture;
    public int id = 1;
    public String name = "Punch";

    public Texture getTexture() {
        if (texture == null) {
            GdxLogHelper.log("texture", texturePath);
            texture = new Texture(Gdx.files.internal(texturePath));
        }
        return texture;
    }

    public CombatProcessor getCombatProcessor() {
        return new PunchProcessor();
    }

    @Override
    public Entity create(World world, Entity source, String group, Position position) {
        Entity e = world.createEntity();

        e.addComponent(new Expiring(0.25f)); // TODO Create ExpiringProcessingSystem to delete once expiration has elapsed.

        float posX = position.x;
        float posY = position.y;

        Directions direction = world.getMapper(Facing.class).get(source).direction;
        if (direction == Directions.UP) {
            posY += 50;
        } else if (direction == Directions.UP_LEFT) {
            posX -= 50;
            posY += 50;
        } else if (direction == Directions.UP_RIGHT) {
            posX += 50;
            posY += 50;
        } else if (direction == Directions.DOWN) {
            posY -= 50;
        } else if (direction == Directions.DOWN_LEFT) {
            posX -= 50;
            posY -= 50;
        } else if (direction == Directions.DOWN_RIGHT) {
            posX += 50;
            posX -= 50;
        } else if (direction == Directions.LEFT) {
            posX -= 50;
        } else if (direction == Directions.RIGHT) {
            posX += 50;
        }

        e.addComponent(new Position(posX, posY));

        return e;
    }
}
