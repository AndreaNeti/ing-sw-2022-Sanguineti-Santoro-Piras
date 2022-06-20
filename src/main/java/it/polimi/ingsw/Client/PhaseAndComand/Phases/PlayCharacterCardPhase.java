package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.ViewGUI;

/**
 * PlayCharacterCardPhase class represents the game phase in which the client can play a character card and add inputs to it.
 */
public class PlayCharacterCardPhase extends ClientPhase {

    /**
     * Constructor PlayCharacterCardPhase creates a new instance of PlayCharacterCardPhase.
     */
    public PlayCharacterCardPhase() {
        super();
    }

    /**
     * //TODO: Method playPhase is empty lol .
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    @Override
    public void playPhase(ViewGUI viewGUI) {

    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Play Character Card Phase".
     */
    @Override
    public String toString() {
        return "Play Character Card Phase";
    }
}
