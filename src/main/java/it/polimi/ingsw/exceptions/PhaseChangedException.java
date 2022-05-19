package it.polimi.ingsw.exceptions;

public class PhaseChangedException extends Exception {
    @Override
    public String getMessage() {
        return "Game phase has changed";
    }
}
