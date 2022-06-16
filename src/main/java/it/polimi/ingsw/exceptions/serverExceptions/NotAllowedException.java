package it.polimi.ingsw.exceptions.serverExceptions;

/**
 * NotAllowedException class represents the exception thrown when a method or command not allowed by the game rules is called. <br>
 * These could be a method called on an invalid component or a command called not during the right phase.
 */
public class NotAllowedException extends GameException {
    private final String message;

    /**
     * Constructor NotAllowedException creates a new intance of NotAllowedException.
     *
     * @param errorMessage of type {@code String} - text message that describes the error.
     */
    public NotAllowedException(String errorMessage) {
        this.message = errorMessage;
    }

    /**
     * Method toString returns the error message.
     *
     * @return {@code String} - error message.
     */
    @Override
    public String getMessage() {
        return message;
    }

}
