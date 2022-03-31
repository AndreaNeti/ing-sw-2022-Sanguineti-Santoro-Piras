package it.polimi.ingsw.exceptions;

public class CardNotFoundException extends GameException {
    @Override
    public String getErrorMessage() {
        return "Card not found";
    }
}
