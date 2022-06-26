package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

/**
 * SelectMatchPhase class represents the game phase in which the client can join a match.
 */
public class SelectMatchPhase extends ClientPhase {

    /**
     * Constructor SelectMatchPhase creates a new instance of SelectMatchPhase.
     */
    public SelectMatchPhase() {
        super();
    }

    /**
     * Method playPhase makes only the match selection box visible, to join a match.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.hideEverything();
        sceneController.getElementById("#selectMatch").setVisible(true);
    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Select Match Phase".
     */
    public String toString() {
        return "Select Match Phase";
    }
}
