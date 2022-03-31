package it.polimi.ingsw.exceptions;

public class NotEnoughStudentsException extends GameException{


    @Override
    public String getErrorMessage() {
        return "Not enough students";
    }
}
