package it.polimi.ingsw.exceptions;

public class UsedCardException extends GameException {
    @Override
    public String getErrorMessage() {
        return "Card already used";
    }
}
