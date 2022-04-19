package it.polimi.ingsw.exceptions;

public class NotEnoughCoinsException extends GameException {
    @Override
    public String getMessage() {
        return "Not Enough Coins";
    }
}
