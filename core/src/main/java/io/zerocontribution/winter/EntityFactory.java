package io.zerocontribution.winter;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import io.zerocontribution.winter.components.AnimationSprite;
import io.zerocontribution.winter.components.Player;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.components.Velocity;

public class EntityFactory {

    public static Entity createPlayer(World world, float x, float y) {
        Entity e = world.createEntity();

        Position position = new Position();
        position.x = x;
        position.y = y;
        e.addComponent(position);

        e.addComponent(new Velocity());

        AnimationSprite sprite = new AnimationSprite();
        sprite.name = "down";
        sprite.width = 18;
        sprite.height = 64;
        sprite.frameTime = 1 / 2f;
        sprite.cols = 8;
        sprite.rows = 1;
        sprite.loop = true;
        sprite.layer = AnimationSprite.Layer.ACTORS_3;
        e.addComponent(sprite);

        e.addComponent(new Player());

        world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYER_AVATAR);

        return e;
    }

}
