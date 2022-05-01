package it.polimi.ingsw.network;

public class TextMessaceSC implements ToClientMessage{
    String message;

    public TextMessaceSC(String message) {
        this.message = message;
    }

}
