package io.zerocontribution.winter.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.Constants;
import io.zerocontribution.winter.ai.pathfinding.*;
import io.zerocontribution.winter.components.*;
import io.zerocontribution.winter.input.InputManager;
import io.zerocontribution.winter.network.AbilityCommand;
import io.zerocontribution.winter.network.ActionCommand;
import io.zerocontribution.winter.systems.client.HudSystem;
import io.zerocontribution.winter.utils.ClientGlobals;
import io.zerocontribution.winter.utils.GdxLogHelper;
import io.zerocontribution.winter.utils.MapHelper;

public class PlayerInputSystem extends EntityProcessingSystem implements InputProcessor {

    private static Mover mover = new Mover();

    @Mapper
    ComponentMapper<Velocity> velocityMapper;

    @Mapper
    ComponentMapper<Position> positionMapper;

    @Mapper
    ComponentMapper<GridPosition> gridPositionMapper;

    @Mapper
    ComponentMapper<TargetGridPosition> targetGridPositionMapper;

    @Mapper
    ComponentMapper<Actor> actorMapper;

    private boolean findTarget;
    private int abilityId = 0;
    private Vector2 newTargetPosition = null;

    IsometricTileMap map;

    Camera camera;

    @SuppressWarnings("unchecked")
    public PlayerInputSystem() {
        super(Aspect.getAspectForAll(LocalPlayer.class, Player.class, Velocity.class));
    }

    @Override
    protected void initialize() {
        InputManager.getInputProcessor().addProcessor(this);

        Entity view = world.getManager(TagManager.class).getEntity(Constants.Tags.VIEW);
        map = new IsometricTileMap(
                ClientGlobals.currentMap.getProperties().get("width", Integer.class),
                ClientGlobals.currentMap.getProperties().get("height", Integer.class),
                world.getMapper(PairMap.class).get(view));

        camera = view.getComponent(Cam.class).camera;
    }

    protected void process(Entity e) {
        updateMovement(e);

        Actor actor = actorMapper.get(e);

        // TODO Not sure if I want to keep this sort of functionality in here?
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
                actor.currentTarget = closest.getId();
            }
            findTarget = false;
        }

        if (abilityId != 0) {
            GdxLogHelper.log("player-input", "Inbound action: " + abilityId);

            ActionInput input = new ActionInput(abilityId, world.getEntity(actor.currentTarget));
            e.addComponent(input);
            e.changedInWorld();

            ClientGlobals.commands.add(AbilityCommand.create(input));

            abilityId = 0;
        }
    }

    private void updateMovement(Entity e) {
        TargetGridPosition targetPosition = targetGridPositionMapper.get(e);
        if (newTargetPosition != null) {
            targetPosition.set(MapHelper.screenToGrid(camera, newTargetPosition.x, newTargetPosition.y));
            newTargetPosition = null;
        }

        if (!targetPosition.hasTarget()) {
            return;
        }

        GridPosition gridPosition = gridPositionMapper.get(e);
        Velocity velocity = velocityMapper.get(e);

        if (targetPosition.at(gridPosition)) {
            targetPosition.reset();
            velocity.set(0, 0);
            sendAllMovementStopActions();
        } else if (targetPosition.hasTarget()) {
            if (targetPosition.pathFinder == null) {
                targetPosition.pathFinder = new AStarPathFinder(map, 100, true, new ManhattanHeuristic());
            }

            if (targetPosition.path == null) {
                try {
                    targetPosition.path = targetPosition.pathFinder.findPath(
                            mover,
                            (int) gridPosition.x,
                            (int) gridPosition.y,
                            (int) targetPosition.x,
                            (int) targetPosition.y
                    );
                    targetPosition.currentPathStep = 1;
                } catch (ArrayIndexOutOfBoundsException aioob) {
                    Log.error("Client", "Input conversion error (" + targetPosition.toLog() + ") " + aioob.toString());
                    targetPosition.reset();
                }
            }

            if (targetPosition.path == null) {
                Log.warn("Client", new StringBuilder()
                        .append("Pathfinding could not find path for ")
                        .append(gridPosition.toLog())
                        .append(" to ")
                        .append(targetPosition.toLog())
                        .toString());
            } else {
                if (targetPosition.path.getLength() == targetPosition.currentPathStep) {
                    targetPosition.path = null;
                    targetPosition.currentPathStep = 1;
                    velocity.set(0, 0);

                    sendAllMovementStopActions();
                } else {
                    Path.Step step = targetPosition.path.getStep(targetPosition.currentPathStep);

                    if (step.getX() > gridPosition.x) {
                        velocity.setX(Constants.PLAYER_SPEED);
                        ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_RIGHT));
                    } else if (step.getX() < gridPosition.x) {
                        velocity.setX(-Constants.PLAYER_SPEED);
                        ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_LEFT));
                    } else {
                        velocity.setX(0);

                        ActionCommand.Action[] actions = {ActionCommand.Action.MOVE_LEFT, ActionCommand.Action.MOVE_RIGHT};
                        sendStopActions(actions);
                    }

                    if (step.getY() > gridPosition.y) {
                        velocity.setY(Constants.PLAYER_SPEED);
                        ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_UP));
                    } else if (step.getY() < gridPosition.y) {
                        velocity.setY(-Constants.PLAYER_SPEED);
                        ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_DOWN));
                    } else {
                        velocity.setY(0);

                        ActionCommand.Action[] actions = {ActionCommand.Action.MOVE_UP, ActionCommand.Action.MOVE_DOWN};
                        sendStopActions(actions);
                    }

                    if (velocity.x == 0 && velocity.y == 0) {
                        targetPosition.currentPathStep++;
                    }
                }
            }
        } else {
            Log.info("Client", "Pathfinding ignoring invalid destination");
        }
    }

    private void sendAllMovementStopActions() {
        ActionCommand.Action[] actions = {
                ActionCommand.Action.MOVE_UP,
                ActionCommand.Action.MOVE_DOWN,
                ActionCommand.Action.MOVE_LEFT,
                ActionCommand.Action.MOVE_RIGHT
        };
        sendStopActions(actions);
    }

    private void sendStopActions(ActionCommand.Action[] actions) {
        for (ActionCommand.Action action : actions) {
            ClientGlobals.commands.add(ActionCommand.stop(action));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        abilityId = 0;
        findTarget = false;

        switch (keycode) {
            case Keys.W:
                ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_UP));
                break;

            case Keys.A:
                ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_LEFT));
                break;

            case Keys.S:
                ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_DOWN));
                break;

            case Keys.D:
                ClientGlobals.commands.add(ActionCommand.start(ActionCommand.Action.MOVE_RIGHT));
                break;

            case Keys.TAB:
                findTarget = true;
                break;

            case Keys.NUM_1:
                abilityId = 1; // TODO
                break;

            case Keys.NUM_2:
            case Keys.NUM_3:
            case Keys.NUM_4:
            case Keys.NUM_5:
                break;

            case Keys.C:
                world.getSystem(HudSystem.class).toggleCharacterWindow();
                break;

            case Keys.ESCAPE:
                world.getSystem(HudSystem.class).toggleInGameMenu();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                ClientGlobals.commands.add(ActionCommand.stop(ActionCommand.Action.MOVE_UP));
                break;

            case Keys.A:
                ClientGlobals.commands.add(ActionCommand.stop(ActionCommand.Action.MOVE_LEFT));
                break;

            case Keys.S:
                ClientGlobals.commands.add(ActionCommand.stop(ActionCommand.Action.MOVE_DOWN));
                break;

            case Keys.D:
                ClientGlobals.commands.add(ActionCommand.stop(ActionCommand.Action.MOVE_RIGHT));
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
        if (button == Input.Buttons.LEFT) {
            newTargetPosition = new Vector2(screenX, screenY);
        }

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
