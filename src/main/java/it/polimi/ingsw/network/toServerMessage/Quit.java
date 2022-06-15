package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;

/**
 * Quit class is used to make the client quit from the game or close the socket connection to quit the application.
 */
public class Quit implements ToServerMessage {

    /**
     * Method execute calls the {@link ClientHandler#quit()} method to make the client quit.
     *
     * @param clientHandler of type ClientHandler - instance of the client handler that sends the message.
     */
    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.quit();
    }
}
