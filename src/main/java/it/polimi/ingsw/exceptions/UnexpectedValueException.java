package it.polimi.ingsw.exceptions;

public class UnexpectedValueException extends GameException{
    @Override
    public String getErrorMessage() {
        return "Unexpected value exception";
    }
}
