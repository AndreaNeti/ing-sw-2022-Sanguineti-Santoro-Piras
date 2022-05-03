package it.polimi.ingsw.network.toClientMessage;

public class TextMessaceSC implements ToClientMessage {
    String message;

    public TextMessaceSC(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message from " + message;
    }
}
