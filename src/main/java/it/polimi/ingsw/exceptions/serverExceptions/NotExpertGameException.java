package it.polimi.ingsw.exceptions.serverExceptions;

/**
 * NotExpertGameException class represents the exception thrown when an expert game method, related to the character cards
 * or the coins logic is played in a normal game.
 */
public class NotExpertGameException extends GameException {

    /**
     * Method getMessage returns the error message.
     *
     * @return {@code String} - "Not expert game".
     */
    @Override
    public String getMessage() {
        return "Not expert game";
    }
}
