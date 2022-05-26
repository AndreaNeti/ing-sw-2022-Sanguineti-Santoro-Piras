package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

public class Quit implements ToServerMessage {

    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        clientHandler.quit();
    }
}
