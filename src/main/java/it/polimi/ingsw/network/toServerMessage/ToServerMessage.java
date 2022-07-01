package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

import java.io.Serializable;

/**
 * ToServerMessage interface represents the messages that the client sends to the server to execute a command. <br>
 * All messages sent by the client to the server implement this interface and have each its own execute method. <br>
 * After executing the command the server will inform the other clients about the action performed by the current client playing.
 */
public interface ToServerMessage extends Serializable {
    /**
     * Method execute is used to execute the ToServerMessage's function through the game controller.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the game is finished or if it's not the client's turn or if the controller throws
     * this exception.
     * @throws EndGameException if the controller throws this exception.
     */
    void execute(ClientHandler clientHandler) throws GameException, EndGameException;
}
