package io.zerocontribution.winter.network;

import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.components.ActionInput;

public class AbilityCommand implements Command {
    public int targetEntityId;
    public int abilityId;

    public static AbilityCommand create(ActionInput actionInput) {
        return new AbilityCommand(
                actionInput.abilityId,
                WinterGame.gameClient.toServerID(actionInput.target.getId())
        );
    }

    public AbilityCommand() {}

    public AbilityCommand(int abilityId) {
        this.abilityId = abilityId;
    }

    public AbilityCommand(int abilityId, int targetEntityId) {
        this.abilityId = abilityId;
        this.targetEntityId = targetEntityId;
    }
}
