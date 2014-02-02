package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.utils.GdxLogHelper;

public class PlayerInputSystem extends EntityProcessingSystem implements InputProcessor {

    @Mapper
    ComponentMapper<Velocity> velocityMapper;

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<Actor> actorMapper;

    @Mapper
    ComponentMapper<Player> playerMapper;

    private InputMultiplexer inputMultiplexer;
    private boolean up, down, left, right, findTarget = false;
    private int abilityId = 0;

    @SuppressWarnings("unchecked")
    public PlayerInputSystem(InputMultiplexer inputMultiplexer) {
        super(Aspect.getAspectForAll(Player.class, Velocity.class));
        this.inputMultiplexer = inputMultiplexer;
    }

    @Override
    protected void initialize() {
        inputMultiplexer.addProcessor(this);
    }

    protected void process(Entity e) {
        Velocity velocity = velocityMapper.get(e);

        velocity.x = 0;
        velocity.y = 0;

        if (left) {
            velocity.x = -Constants.PLAYER_SPEED;
        } else if (right) {
            velocity.x = Constants.PLAYER_SPEED;
        }

        if (up) {
            velocity.y = Constants.PLAYER_SPEED;
        } else if (down) {
            velocity.y = -Constants.PLAYER_SPEED;
        }

        Actor actor = actorMapper.get(e);

        // TODO Not sure if I want to keep this sort of functionality in here?
        // It doesn't sem
        if (findTarget) {
            // TODO It would be good to have a utility class for sorting ImmutableBags.
            ImmutableBag<Entity> enemies = world.getManager(GroupManager.class).getEntities(Constants.Groups.ENEMIES);
            Position playerPosition = positionMapper.get(e);

            Entity closest = null;
            float closestDst = -1;
            for (int i = 0; i < enemies.size(); i++) {
                Position enemyPosition = positionMapper.get(enemies.get(i));

                float dst = (float) Math.hypot(
                        Math.abs(enemyPosition.x - playerPosition.x),
                        Math.abs(enemyPosition.y - playerPosition.y));

                if (closestDst > dst || closestDst == -1) {
                    closest = enemies.get(i);
                    closestDst = dst;
                }
            }
            if (closest != null) {
                GdxLogHelper.log("player-input", "selected closest enemy, " + closest);
                actor.currentTarget = closest;
            }
            findTarget = false;
        }

        if (abilityId != 0) {
            GdxLogHelper.log("player-input", "Inbound action: " + abilityId);
            Player player = playerMapper.get(e);
            e.addComponent(new ActionInput(player.group, abilityId, actor.currentTarget));
            e.changedInWorld();
            abilityId = 0;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        abilityId = 0;
        findTarget = false;

        switch (keycode) {
            case Keys.W:
                up = true;
                down = false;
                break;

            case Keys.A:
                left = true;
                right = false;
                break;

            case Keys.S:
                down = true;
                up = false;
                break;

            case Keys.D:
                right = true;
                left = false;
                break;

            case Keys.TAB:
                findTarget = true;
                break;

            case Keys.NUM_1:
                abilityId = 1; // TODO
                break;

            case Keys.C:
                // TODO Character HUD
                break;

            case Keys.I:
                world.getSystem(HudSystem.class).toggleInventory();
                break;

            case Keys.L:
                // TODO Quest Log HUD
                break;

            case Keys.ESCAPE:
                // TODO Menu HUD
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                up = false;
                break;

            case Keys.A:
                left = false;
                break;

            case Keys.S:
                down = false;
                break;

            case Keys.D:
                right = false;
                break;

            case Keys.TAB:
                findTarget = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
