package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

public class OK implements ToClientMessage {
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMessage("Server: Successful operation");
        controllerClient.setNextClientPhase();
    }
}
