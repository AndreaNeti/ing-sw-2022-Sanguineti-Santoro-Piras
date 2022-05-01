package it.polimi.ingsw.network;

public class ErrorException implements ToClientMessage{
    String error;

    public ErrorException(String error) {
        this.error = error;
    }

}
