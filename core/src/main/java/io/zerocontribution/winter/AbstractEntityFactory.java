package io.zerocontribution.winter;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.zerocontribution.winter.ai.AIRegistry;
import io.zerocontribution.winter.ai.normals.ZombieAI;
import io.zerocontribution.winter.assets.EnemyAsset;
import io.zerocontribution.winter.combat.abilities.Ability;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.struct.Delay;
import io.zerocontribution.winter.struct.Directions;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;

public abstract class AbstractEntityFactory {

    public abstract Entity createMap(World world);

    public abstract Entity createBlockingTile(World world, float x, float y);

}
