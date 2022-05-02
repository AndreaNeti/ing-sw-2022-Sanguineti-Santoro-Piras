package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

import java.io.Serializable;

public interface ToServerMessage extends Serializable {
    void execute(PlayerHandler playerHandler) throws GameException;
}
