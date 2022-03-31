package it.polimi.ingsw.exceptions;

public class NotExpertGameException extends GameException{

    @Override
    public String getErrorMessage() {
        return "Not expert game";
    }
}
