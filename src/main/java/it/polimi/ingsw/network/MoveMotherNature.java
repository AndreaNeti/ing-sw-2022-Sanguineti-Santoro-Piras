package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class MoveMotherNature implements ToServerMessage{
    int moves;

    public MoveMotherNature(int moves) {
        this.moves = moves;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.getController().moveMotherNature(moves);
    }

}
