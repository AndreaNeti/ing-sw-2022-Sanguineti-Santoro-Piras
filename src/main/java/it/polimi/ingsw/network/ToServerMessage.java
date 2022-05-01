package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public interface ToServerMessage {
    void execute(PlayerHandler playerHandler) throws GameException;
}
