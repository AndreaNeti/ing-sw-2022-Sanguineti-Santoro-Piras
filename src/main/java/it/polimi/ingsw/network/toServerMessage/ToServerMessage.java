package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

import java.io.Serializable;

public interface ToServerMessage extends Serializable {
    void execute(ClientHandler clientHandler) throws GameException, EndGameException;
}
