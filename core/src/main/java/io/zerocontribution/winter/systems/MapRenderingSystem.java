package io.zerocontribution.winter.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.Cam;
import io.zerocontribution.winter.components.MapView;

public class MapRenderingSystem extends VoidEntitySystem {

    @Mapper
    ComponentMapper<Cam> camMapper;

    @Mapper
    ComponentMapper<MapView> mapViewMapper;

    Entity view;

    @Override
    protected void initialize() {
        view = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
    }

    @Override
    protected void processSystem() {
        Cam cam = camMapper.get(view);
        MapView mapView = mapViewMapper.get(view);
        mapView.renderer.setView(cam.camera);
        mapView.renderer.render();
    }
}
