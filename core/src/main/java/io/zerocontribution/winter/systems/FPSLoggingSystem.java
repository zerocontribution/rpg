package io.zerocontribution.winter.systems;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.FPSLogger;

public class FPSLoggingSystem extends VoidEntitySystem {

    private FPSLogger logger;

    public FPSLoggingSystem() {
        logger = new FPSLogger();
    }

    @Override
    protected void processSystem() {
        logger.log();
    }

}
