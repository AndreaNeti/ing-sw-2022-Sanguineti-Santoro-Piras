package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

public class ErrorException implements ToClientMessage {
    String error;

    public ErrorException(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Error: " + error;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMessage("Server error: " + error);
        controllerClient.error();
    }
}
