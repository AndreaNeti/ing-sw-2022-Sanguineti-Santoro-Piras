package it.polimi.ingsw.client.phaseAndComand.Phases;

import it.polimi.ingsw.client.view.gui.GuiFX;
import it.polimi.ingsw.client.view.gui.SceneController.SceneController;
import it.polimi.ingsw.client.view.gui.ViewGUI;


/**
 * InitPhase class represents the game phase in which the client can connect to a server.
 */
public class InitPhase extends ClientPhase {

    /**
     * Constructor InitPhase creates a new instance of InitPhase.
     */
    public InitPhase() {
        super();
    }

    /**
     * Method playPhase makes only the chat visible and usable to connect to a server.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.hideEverything();
        sceneController.getElementById("#connect").setVisible(true);
    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Init Phase".
     */
    @Override
    public String toString() {
        return "Init Phase";
    }
}
