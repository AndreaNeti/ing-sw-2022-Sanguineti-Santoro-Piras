package it.polimi.ingsw.exceptions.serverExceptions;

public class NotExpertGameException extends GameException {
    @Override
    public String getMessage() {
        return "Not expert game";
    }
}
