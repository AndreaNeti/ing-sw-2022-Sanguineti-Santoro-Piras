package it.polimi.ingsw.client.phaseAndComand.Phases;

import it.polimi.ingsw.client.view.Gui.GuiFX;
import it.polimi.ingsw.client.view.Gui.SceneController.SceneController;
import it.polimi.ingsw.client.view.Gui.ViewGUI;

/**
 * NicknamePhase class represents the game phase in which the client can choose the player's nickname.
 */
public class NicknamePhase extends ClientPhase {

    /**
     * Constructor NicknamePhase creates a new instance of NicknamePhase.
     */
    public NicknamePhase() {
        super();
    }

    /**
     * Method playPhase makes only the nickname box visible, to select a nickname.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.hideEverything();
        sceneController.getElementById("#nickName").setVisible(true);
    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Nickname Phase".
     */
    @Override
    public String toString() {
        return "Nickname Phase";
    }
}