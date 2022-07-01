package it.polimi.ingsw.network;

import it.polimi.ingsw.client.Controller.ControllerClient;
import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

/**
 * PingMessage class is used by both the client and the server to reset the ping timeout timer in order not to close the connection.
 */
public class PingMessage implements ToClientMessage, ToServerMessage {

    /**
     * Method resetPing updates the time of the last ping received to the current one in the timeout timer ({@link PingPong}) of the listener.
     *
     * @param pingPongInterface of type {@link PingPongInterface} - instance of the ControllerClient (Client side) / ClientHandler (Server side).
     */
    private void resetPing(PingPongInterface pingPongInterface) {
        pingPongInterface.resetPing();
    }

    /**
     * Method execute is used by the client's controller to reset its timeout timer.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        resetPing(controllerClient);
    }

    /**
     * Method execute is used by the client handler to reset its timeout timer.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     */
    @Override
    public void execute(ClientHandler clientHandler){
        resetPing(clientHandler);
    }
}
