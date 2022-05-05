package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class ChooseCharacter implements ToServerMessage {
    byte character;

    public ChooseCharacter(byte character) {
        this.character = character;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        Controller c = playerHandler.getController();
        if(c.isGameFinished()){
            throw new NotAllowedException("Game is already finished");
        }
        if (c.isMyTurn(playerHandler))
            c.chooseCharacter(character);
        else throw new NotAllowedException("It's not your turn");
    }
}
