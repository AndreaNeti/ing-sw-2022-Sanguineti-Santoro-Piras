package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.model.Color;

public class Move implements ToServerMessage {
    Color color;
    int idGameComponent;

    public Move(Color color, int idGameComponent) {
        this.color = color;
        this.idGameComponent = idGameComponent;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        Controller c = playerHandler.getController();
        if (c.isMyTurn(playerHandler))
            c.move(color, idGameComponent);
        else throw new NotAllowedException("It's not your turn");
    }
}
