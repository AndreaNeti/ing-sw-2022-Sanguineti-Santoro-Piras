package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.server.controller.ClientHandler;

/**
 * TextMessageCS class is used to send a text message from the client to the server, which then forwards it
 * to all the other clients.
 */
public class TextMessageCS implements ToServerMessage {
    private final String message;

    /**
     * Constructor TextMessageCS creates a new instance of TextMessageCS.
     *
     * @param message of type {@code String} - text message to send to other clients.
     */
    public TextMessageCS(String message) {
        this.message = message;
    }

    /**
     * Method execute uses the game controller to send the text message to the other clients.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     */
    @Override
    public void execute(ClientHandler clientHandler) {
        clientHandler.getController().sendMessage(clientHandler, message);
    }
}
