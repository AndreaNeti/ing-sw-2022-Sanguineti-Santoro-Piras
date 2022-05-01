package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class PlayCharacter implements ToServerMessage{
    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.getController().playCharacter();
    }
}
