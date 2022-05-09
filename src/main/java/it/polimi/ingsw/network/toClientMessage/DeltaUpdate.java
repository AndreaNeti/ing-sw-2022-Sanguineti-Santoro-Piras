package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Server.controller.GameDelta;

public class DeltaUpdate implements ToClientMessage {
    GameDelta gameDelta;

    public DeltaUpdate(GameDelta gameDelta) {
        this.gameDelta = gameDelta;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.changeGame(gameDelta);
    }
}
