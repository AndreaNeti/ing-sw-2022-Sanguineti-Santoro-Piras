package it.polimi.ingsw.exceptions.serverExceptions;

public class EndGameException extends Exception {
    private final boolean endInstantly;

    public EndGameException(boolean endInstantly) {
        this.endInstantly = endInstantly;
    }

    public boolean isEndInstantly() {
        return endInstantly;
    }
}
