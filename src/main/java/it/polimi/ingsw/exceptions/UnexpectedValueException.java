package it.polimi.ingsw.exceptions;

public class UnexpectedValueException extends GameException {
    @Override
    public String getMessage() {
        return "Unexpected value exception";
    }
}
