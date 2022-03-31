package it.polimi.ingsw.exceptions;

public class NotEnoughCoinsException extends GameException{
    @Override
    public String getErrorMessage() {
        return "Not Enough Coins";
    }
}
