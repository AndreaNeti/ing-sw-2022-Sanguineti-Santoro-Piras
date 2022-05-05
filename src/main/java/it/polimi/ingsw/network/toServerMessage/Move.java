package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.model.Color;

public class Move implements ToServerMessage {
    Color color;
    int indexGameComponent;

    public Move(Color color, int indexGameComponent) {
        this.color = color;
        this.indexGameComponent = indexGameComponent;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        Controller c = playerHandler.getController();
        if(c.isGameFinished()){
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(playerHandler))
            c.move(color, indexGameComponent);
        else throw new NotAllowedException("It's not your turn");
    }
}
