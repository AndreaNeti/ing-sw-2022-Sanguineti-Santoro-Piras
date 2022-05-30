package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;

public class TextMessageCS implements ToServerMessage {
    private final String message;

    public TextMessageCS(String message) {
        this.message = message;
    }

    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.getController().sendMessage(clientHandler, message);
    }
}
