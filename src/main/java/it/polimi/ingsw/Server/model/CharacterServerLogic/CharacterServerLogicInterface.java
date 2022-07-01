package it.polimi.ingsw.Server.model.CharacterServerLogic;

import it.polimi.ingsw.Server.model.GameInterfaceForCharacter;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

/**
 * CharacterServerLogicInterface interface represents the character cards server logic in "Eriantys", available in the expert game mode. <br>
 * It contains the methods required to apply the cards' effects. <br>
 * There are a total of 12 character cards classes that implement this interface, each one with its own effect on the game and required inputs.
 */
public interface CharacterServerLogicInterface {
    /**
     * Method play applies the card's effect to the game, using the CharacterCardGame interface's functions.
     *
     * @param game of type {@link GameInterfaceForCharacter} - the game instance that the card modifies with its effect.
     * @throws GameException    if the inputs provided are wrong or invalid.
     * @throws EndGameException if the character card's effect trigger an endgame event (no more students in the bag,
     *                          no more towers in a team's board or less than 3 islands left)
     */
    void play(GameInterfaceForCharacter game) throws GameException, EndGameException;

    /**
     * Method canPlay checks if the card can be played with the number of inputs provided by the player.
     *
     * @param nInput of type {@code int} - number of inputs provided.
     * @return {@code boolean} - true if the card can be played, false else.
     */
    boolean canPlay(int nInput);
}
