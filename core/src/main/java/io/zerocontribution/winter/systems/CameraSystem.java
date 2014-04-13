package io.zerocontribution.winter.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.*;

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
        return player != null && positionMapper.has(player);
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
