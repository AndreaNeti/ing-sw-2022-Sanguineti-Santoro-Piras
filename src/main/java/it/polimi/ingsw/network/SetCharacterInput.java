package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class SetCharacterInput implements ToServerMessage {
    int input;

    public SetCharacterInput(int input) {
        this.input = input;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        Controller c = playerHandler.getController();
        if (c.isMyTurn(playerHandler))
            c.setCharacterInput(input);
        else throw new NotAllowedException("It's not your turn");
    }
}