package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

import java.io.Serializable;

public interface ToServerMessage extends Serializable {
    void execute(PlayerHandler playerHandler) throws GameException;
}
