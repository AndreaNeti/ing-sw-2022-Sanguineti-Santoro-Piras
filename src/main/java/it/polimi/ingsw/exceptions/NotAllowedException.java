package it.polimi.ingsw.exceptions;

public class NotAllowedException extends GameException {
    private final String message;

    public NotAllowedException(String errorMessage) {
        this.message = errorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
