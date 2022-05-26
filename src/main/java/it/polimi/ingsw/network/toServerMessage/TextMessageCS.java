package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

public class TextMessageCS implements ToServerMessage {
    String message;

    public TextMessageCS(String message) {
        this.message = message;
    }

    @Override
    public void execute(ClientHandler clientHandler) throws GameException, EndGameException {
        clientHandler.getController().sendMessage(clientHandler, message);
    }
}
