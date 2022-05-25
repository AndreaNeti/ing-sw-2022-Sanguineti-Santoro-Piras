package it.polimi.ingsw.exceptions.serverExceptions;

public class UsedCardException extends GameException {
    @Override
    public String getMessage() {
        return "Card already used";
    }
}
