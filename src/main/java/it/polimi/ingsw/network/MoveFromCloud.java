package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class MoveFromCloud implements ToServerMessage {
    public MoveFromCloud(int idGameComponent) {
        this.idGameComponent = idGameComponent;
    }

    int idGameComponent;

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.getController().moveFromCloud(idGameComponent);
    }
}