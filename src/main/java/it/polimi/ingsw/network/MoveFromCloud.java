package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class MoveFromCloud implements ToServerMessage {
    public MoveFromCloud(int idGameComponent) {
        this.idGameComponent = idGameComponent;
    }

    int idGameComponent;

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        Controller c = playerHandler.getController();
        if (c.isMyTurn(playerHandler))
            c.moveFromCloud(idGameComponent);
        else throw new NotAllowedException("It's not your turn");
    }
}