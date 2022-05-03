package it.polimi.ingsw.network.toClientMessage;

public class ErrorException implements ToClientMessage {
    String error;

    public ErrorException(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Error: " + error;
    }
}
