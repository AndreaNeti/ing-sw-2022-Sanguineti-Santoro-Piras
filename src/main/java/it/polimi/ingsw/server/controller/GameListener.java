package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

/**
 * GameListener interface is used to send messages to the client to inform it when the game is updated. <br>
 * This interface is implemented by ClientHandler, used to interface the clients with the server's game model.
 */
public interface GameListener {
    /**
     * Method update sends a message from the server to the client via socket connection.
     *
     * @param message of type {@link ToClientMessage} - message to send to the client.
     */
    void update(ToClientMessage message);
}
