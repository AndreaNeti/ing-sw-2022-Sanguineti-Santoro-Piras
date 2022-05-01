package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class ChooseCharacter implements ToServerMessage {
    byte character;

    public ChooseCharacter(byte character) {
        this.character = character;
    }
    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.getController().chooseCharacter(character);
    }
}
