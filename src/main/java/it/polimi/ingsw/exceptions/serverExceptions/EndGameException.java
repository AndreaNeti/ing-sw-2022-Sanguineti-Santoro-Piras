package it.polimi.ingsw.exceptions.serverExceptions;

/**
 * EndGameException class represents the exception thrown when an event should trigger the end of the game. <br>
 * The game can end instantly (a team with no more tower left or less than 3 islands left)
 * or last for one more final round (no more students in the bag or no more assistant cards left). <br>
 * This information is stored inside the exception.
 */
public class EndGameException extends Exception {
    private final boolean endInstantly;

    /**
     * Constructor EndGameException creates a new instance of EndGameException
     *
     * @param endInstantly of type {@code boolean} - boolean to check if the game should end instantly or not.
     */
    public EndGameException(boolean endInstantly) {
        this.endInstantly = endInstantly;
    }

    /**
     * Method isEndInstantly checks if the game should end instantly.
     *
     * @return {@code boolean} - true if the game should end instantly, false else.
     */
    public boolean isEndInstantly() {
        return endInstantly;
    }
}
