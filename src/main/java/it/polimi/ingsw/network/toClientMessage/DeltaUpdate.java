package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Server.controller.GameDelta;

public class DeltaUpdate implements ToClientMessage {
    GameDelta gameDelta;

    public DeltaUpdate(GameDelta gameDelta) {
        this.gameDelta = gameDelta;
    }

}
