package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

public class TextMessageSC implements ToClientMessage {
    String message;

    public TextMessageSC(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message from " + message;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        System.out.println(message);
    }
}