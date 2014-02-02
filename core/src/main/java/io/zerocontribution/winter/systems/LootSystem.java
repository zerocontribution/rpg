package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import io.zerocontribution.winter.Assets;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;

/**
 * @todo This should be redesigned to work off of player interaction, as opposed to auto-pickups.
 */
public class LootSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Bounds> boundsMapper;

    @Mapper
    ComponentMapper<Drop> dropMapper;

    @Mapper
    ComponentMapper<Inventory> inventoryMapper;

    @SuppressWarnings("unchecked")
    public LootSystem() {
        super(Aspect.getAspectForAll(Drop.class, Bounds.class));
    }

    @Override
    protected boolean checkProcessing() {
        return world.getManager(GroupManager.class).getEntities(Constants.Groups.DROPS).size() > 0;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        Entity player = world.getManager(TagManager.class).getEntity(Constants.Tags.PLAYER);
        Bounds playerBounds = boundsMapper.get(player);

        for (int i = 0; i < entities.size(); i++) {
            Entity loot = entities.get(i);
            if (checkCollision(playerBounds, loot)) {
                pickUpLoot(player, loot);
            }
        }
    }

    private boolean checkCollision(Bounds playerBounds, Entity loot) {
        return playerBounds.rect.overlaps(boundsMapper.get(loot).rect);
    }

    private void pickUpLoot(Entity player, Entity loot) {
        Drop drop = dropMapper.get(loot);
        Inventory inventory = inventoryMapper.get(player);

        if (drop.credits > 0) {
            inventory.credits += drop.credits;
            GdxLogHelper.log("loot", "player gained " + drop.credits + " credits");
            // TODO Notification of new credits
        }

        if (drop.itemId > 0) {
            Item item = Assets.items.get(drop.itemId);

            if (inventory.addItem(item)) {
                GdxLogHelper.log("loot", "Picked up item " + drop.itemId);
            } else {
                GdxLogHelper.log("loot", "Could not pick up item " + drop.itemId);
            }

            // TODO Notification of new item
        }

        world.deleteEntity(loot);
    }

}
