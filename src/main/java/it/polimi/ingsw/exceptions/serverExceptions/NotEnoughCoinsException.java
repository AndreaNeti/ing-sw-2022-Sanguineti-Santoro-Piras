package it.polimi.ingsw.exceptions.serverExceptions;

/**
 * NotEnoughCoinsException class represents the exception thrown then a player doesn't have enough coins to play a card
 * or when the game doesn't have enough coins left to give to the player that should obtain them.
 */
public class NotEnoughCoinsException extends GameException {

    /**
     * Method getMessage returns the error message.
     *
     * @return {@code String} - "Not enough coins".
     */
    @Override
    public String getMessage() {
        return "Not enough coins";
    }
}
