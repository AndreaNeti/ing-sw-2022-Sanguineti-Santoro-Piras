package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

/**
 * CharacterCardClient interface represents the character cards in "Eriantys", available in the expert game mode. <br>
 * This interface is used client side to access the info of the character cards sent by the server.
 * There are a total of 12 client classes that implement this interface.
 */
public interface CharacterCardClient extends CharacterCardDataInterface {
    //TODO: add toString as function to override to avoid writing docs for all subclasses.

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
     * Method setData sets the card data.
     *
     * @param data of type {@link CharacterCardDataInterface} - the data to set into the card.
     */
    void setData(CharacterCardDataInterface data);

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
