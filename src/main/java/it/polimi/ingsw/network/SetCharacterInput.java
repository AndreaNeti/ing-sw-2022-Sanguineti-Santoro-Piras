package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class SetCharacterInput implements ToServerMessage {
    int input;

    public SetCharacterInput(int input) {
        this.input = input;
    }

    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.getController().setCharacterInput(input);
    }
}