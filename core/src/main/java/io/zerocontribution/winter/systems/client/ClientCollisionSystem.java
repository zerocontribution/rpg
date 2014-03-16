package io.zerocontribution.winter.systems.client;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.struct.Pair;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.systems.common.AbstractCollisionSystem;
import io.zerocontribution.winter.utils.ClientGlobals;

public class ClientCollisionSystem extends AbstractCollisionSystem {

    @Mapper
    ComponentMapper<PairMap> pairMapMapper;

    public ClientCollisionSystem() {
        super();
    }

    protected void addCollisionTiles() {
        Entity view = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
        PairMap pairMap = pairMapMapper.get(view);
        pairMap.map.clear();

        TiledMap map = ClientGlobals.currentMap;
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("Ground");

        float mapW = (float) map.getProperties().get("width", Integer.class);
        float mapH = (float) map.getProperties().get("height", Integer.class);

        // TODO Rotate bounding boxes for blocks 90*
        // TODO ??? Default to squares; check for texture region?
        for (int y = -1; y <= mapH; y++) {
            for (int x = -1; x <= mapW; x++) {
                if (pairMap.map.get(Pair.get(x, y)) == null) {
                    TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                    if (cell == null || cell.getTile().getProperties().containsKey(Constants.TILE_OBSTACLE)) {
                        ClientGlobals.entityFactory.createBlockingTile(world, x, y).addToWorld();
                        pairMap.map.put(Pair.get(x, y), new Boolean(true));
                    }
                }
            }
        }
    }

}
