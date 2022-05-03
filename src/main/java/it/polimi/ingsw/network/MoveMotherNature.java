package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class MoveMotherNature implements ToServerMessage {
    int moves;

    public MoveMotherNature(int moves) {
        this.moves = moves;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        Controller c = playerHandler.getController();
        if (c.isMyTurn(playerHandler))
            c.moveMotherNature(moves);
        else throw new NotAllowedException("It's not your turn");
    }

}
