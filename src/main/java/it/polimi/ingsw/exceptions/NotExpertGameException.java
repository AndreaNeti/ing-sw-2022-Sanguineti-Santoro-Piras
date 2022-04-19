package it.polimi.ingsw.exceptions;

public class NotExpertGameException extends GameException {
    @Override
    public String getMessage() {
        return "Not expert game";
    }
}
