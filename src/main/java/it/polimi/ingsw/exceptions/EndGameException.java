package it.polimi.ingsw.exceptions;

public class EndGameException extends Exception {
    private final boolean endInstantly;

    public EndGameException(boolean endInstantly) {
        this.endInstantly = endInstantly;
    }

    public boolean isEndInstantly() {
        return endInstantly;
    }
}
