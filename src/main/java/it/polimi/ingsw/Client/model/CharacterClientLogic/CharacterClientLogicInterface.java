package it.polimi.ingsw.Client.model.CharacterClientLogic;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

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
