package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

/**
 * OK class is used to send a "successful operation" message to the client.
 */
public class OK implements ToClientMessage {

    /**
     * Method execute used the client controller to add a "successful operation" message to its chat and set the
     * next phase.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMessage("Server: Successful operation");
        controllerClient.setNextClientPhase();
    }
}
