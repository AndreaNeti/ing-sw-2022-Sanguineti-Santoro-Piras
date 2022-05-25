package it.polimi.ingsw.exceptions.serverExceptions;

public class NotEnoughCoinsException extends GameException {
    @Override
    public String getMessage() {
        return "Not Enough Coins";
    }
}
