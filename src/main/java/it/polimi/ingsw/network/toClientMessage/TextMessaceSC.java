package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

public class TextMessaceSC implements ToClientMessage {
    String message;

    public TextMessaceSC(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message from " + message;
    }

    @Override
    public void execute(ControllerClient controllerClient) {

    }
}
