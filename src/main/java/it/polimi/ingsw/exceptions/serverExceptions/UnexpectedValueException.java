package it.polimi.ingsw.exceptions.serverExceptions;

public class UnexpectedValueException extends GameException {
    @Override
    public String getMessage() {
        return "Unexpected value";
    }
}
