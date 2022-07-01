package it.polimi.ingsw.client.View.Cli;

import it.polimi.ingsw.util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

/**
 * ViewForCharacterCli interface is a CLI view with only functions required by character cards to add inputs to them.
 */
public interface ViewForCharacterCli {

    /**
     * Method getColorInput is used add a student color to the list of card inputs.
     *
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@link Color} - student color to add to the list of inputs.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
    Color getColorInput(boolean canBeStopped) throws SkipCommandException;

    /**
     * Method getIslandDestination is used add an island destination to the list of card inputs.
     *
     * @param message of type {@code String} - message that will ask the user to input data.
     * @param canBeStopped of type {@code Boolean} - boolean to check if the method can be stopped before the user inputs all the
     *                     required info.
     * @return {@code int} - ID of the island of destination to add to the list of inputs.
     * @throws SkipCommandException if the method ends and the user's input must be skipped (input not repeated and not ready).
     */
    int getIslandDestination(String message, boolean canBeStopped) throws SkipCommandException;
}
