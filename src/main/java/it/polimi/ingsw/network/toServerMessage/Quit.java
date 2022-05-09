package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;

public class Quit implements ToServerMessage {

    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.quit();
    }
}
