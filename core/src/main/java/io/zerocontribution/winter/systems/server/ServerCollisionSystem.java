package io.zerocontribution.winter.systems.server;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.struct.Pair;
import io.zerocontribution.winter.components.PairMap;
import io.zerocontribution.winter.server.maps.tiled.TiledMap;
import io.zerocontribution.winter.server.maps.tiled.TiledMapTileLayer;
import io.zerocontribution.winter.systems.common.AbstractCollisionSystem;
import io.zerocontribution.winter.utils.ServerGlobals;

public class ServerCollisionSystem extends AbstractCollisionSystem {

    @Mapper
    ComponentMapper<PairMap> pairMapMapper;

    Entity view;

    public ServerCollisionSystem() {
        super();
    }

    @Override
    protected boolean checkProcessing() {
        if (view == null) {
            view = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
            return view != null;
        }

        return true;
    }

    protected void addCollisionTiles() {
        PairMap pairMap = pairMapMapper.get(view);
        pairMap.map.clear();

        TiledMap map = ServerGlobals.currentMap;
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("Ground");

        float mapW = (float) map.getProperties().get("width", Integer.class);
        float mapH = (float) map.getProperties().get("height", Integer.class);

        // TODO Rotate bounding boxes for blocks 90*
        // TODO ??? Default to squares; check cell h/w?
        for (int y = -1; y <= mapH; y++) {
            for (int x = -1; x <= mapW; x++) {
                if (pairMap.map.get(Pair.get(x, y)) == null) {
                    TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                    if (cell == null || cell.getTile().getProperties().containsKey(Constants.TILE_OBSTACLE)) {
                        ServerGlobals.entityFactory.createBlockingTile(world, x, y).addToWorld();
                        pairMap.map.put(Pair.get(x, y), new Boolean(true));
                    }
                }
            }
        }
    }
}
