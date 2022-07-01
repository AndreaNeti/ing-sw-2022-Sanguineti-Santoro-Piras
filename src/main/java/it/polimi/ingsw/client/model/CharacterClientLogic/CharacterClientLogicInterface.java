package it.polimi.ingsw.client.model.CharacterClientLogic;

import it.polimi.ingsw.client.view.cli.ViewForCharacterCli;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

/**
 * CharacterServerLogicInterface interface represents the character cards client logic in "Eriantys", available in the expert game mode. <br>
 * It contains the methods required to obtain the inputs to apply the cards' effects and to get a description about them. <br>
 * There are a total of 12 character cards classes that implement this interface, each one with its own effect on the game and required inputs.
 */
public interface CharacterClientLogicInterface {
    /**
     * Method getDescription returns the description of the effect of the card.
     *
     * @return {@code String} - card description.
     */
    String getDescription();

    /**
     * Method setNextInput requests the client's view to add inputs to the card.
     *
     * @param view of type {@link ViewForCharacterCli} - client's CLI view from which the inputs are added.
     * @throws SkipCommandException if
     */
    //return the next input needed for the character card, null if it can be played
    void setNextInput(ViewForCharacterCli view) throws SkipCommandException;

    /**
     * Method setHandler enables the GUI components necessary to apply the card effect.
     *
     * @param viewGUI of type {@link ViewGUI} - client's GUI view on which the required nodes are enabled.
     */
    void setHandler(ViewGUI viewGUI);

    /**
     * Method canPlay checks if the card has the exact number of inputs required.
     *
     * @return {@code boolean} - true if the number of input is the required amount to play the card, false else.
     */
    boolean canPlay();

    /**
     * Method isFull checks if the card can receive more inputs.
     *
     * @return {@code boolean} - true if the card has already the amount of inputs required, false else.
     */
    boolean isFull();

    /**
     * Method resetInput clears the list of inputs added to the card.
     */
    void resetInput();

    /**
     * Method getInputs returns the list of inputs added to the card.
     *
     * @return {@code List}<{@code Integer}> - list of inputs added to the card.
     */
    List<Integer> getInputs();

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    String toString();
}
