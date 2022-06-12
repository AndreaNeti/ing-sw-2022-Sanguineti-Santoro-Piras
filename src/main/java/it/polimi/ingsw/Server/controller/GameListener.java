package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

/**
 * GameListener interface is used to send messages to the client to inform it when the game is updated. <br>
 * This interface is implemented by ClientHandler, used to interface the clients with the server's game model.
 */
public interface GameListener {
    //TODO: check if we can remove this interface
    /**
     * Method update sends a message from the server to the client via socket connection.
     *
     * @param message of type ToClientMessage - message to send to the client.
     */
    void update(ToClientMessage message);
}
