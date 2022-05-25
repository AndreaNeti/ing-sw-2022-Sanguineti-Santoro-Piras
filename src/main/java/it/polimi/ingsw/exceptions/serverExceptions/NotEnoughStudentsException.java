package it.polimi.ingsw.exceptions.serverExceptions;

public class NotEnoughStudentsException extends GameException {
    @Override
    public String getMessage() {
        return "Not enough students";
    }
}
