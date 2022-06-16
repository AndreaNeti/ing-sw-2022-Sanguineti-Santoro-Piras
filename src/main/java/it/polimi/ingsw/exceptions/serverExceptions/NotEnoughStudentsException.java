package it.polimi.ingsw.exceptions.serverExceptions;

/**
 * NotEnoughStudentsException class represents the exception thrown when a component doesn't have enough students of the
 * selected color to move.
 */
public class NotEnoughStudentsException extends GameException {

    /**
     * Method getMessage returns the error message.
     *
     * @return {@code String} - "Not enough students".
     */
    @Override
    public String getMessage() {
        return "Not enough students";
    }
}
