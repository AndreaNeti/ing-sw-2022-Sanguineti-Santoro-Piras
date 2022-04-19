package it.polimi.ingsw.exceptions;

public class UsedCardException extends GameException {
    @Override
    public String getMessage() {
        return "Card already used";
    }
}
