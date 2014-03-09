package io.zerocontribution.winter.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;

public class CameraSystem extends VoidEntitySystem {

    @Mapper
    ComponentMapper<Cam> camMapper;

    @Mapper
    ComponentMapper<Position> positionMapper;

    private Entity player;
    private Entity camera;

    @Override
    public void initialize() {
        player = world.getManager(TagManager.class).getEntity(Constants.Tags.LOCAL_PLAYER);
        camera = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
    }

    @Override
    protected boolean checkProcessing() {
        if (player != null) {
            return true;
        }

        player = world.getManager(TagManager.class).getEntity(Constants.Tags.LOCAL_PLAYER);
        if (player != null && positionMapper.has(player)) {
            return true;
        }

        return false;
    }

    @Override
    protected void processSystem() {
        Cam cam = camMapper.get(camera);
        Position position = positionMapper.get(player);
        cam.camera.position.x = position.x;
        cam.camera.position.y = position.y;
        cam.camera.update();
    }

}
