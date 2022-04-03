package it.polimi.ingsw.exceptions;

public class EndGameException extends Exception {
    private final boolean endIstantly;

    public EndGameException(boolean endIstantly) {
        this.endIstantly = endIstantly;
    }

    public boolean isEndIstantly() {
        return endIstantly;
    }
}
