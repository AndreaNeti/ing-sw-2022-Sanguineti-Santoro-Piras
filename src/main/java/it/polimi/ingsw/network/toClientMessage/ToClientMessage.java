package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.client.controller.ControllerClient;

import java.io.Serializable;

/**
 * ToClientMessage interface represents the messages that the server sends to the client to execute a command. <br>
 * All messages sent by the server to the client implement this interface and have each its own execute method. <br>
 */
public interface ToClientMessage extends Serializable {
    /**
     * Method execute is used to execute the ToClientMessage's function through the client controller.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    void execute(ControllerClient controllerClient);
}
