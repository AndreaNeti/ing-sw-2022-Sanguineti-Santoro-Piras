package it.polimi.ingsw.client.PhaseAndComand.Phases;

import it.polimi.ingsw.client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.client.View.Gui.GuiFX;
import it.polimi.ingsw.client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.client.View.Gui.ViewGUI;

/**
 * MoveStudentsPhase class represents the game phase in which the client can move students.
 */
public class MoveStudentsPhase extends ClientPhase {

    /**
     * Constructor MoveStudentsPhase creates a new instance of MoveStudentsPhase.
     */
    public MoveStudentsPhase() {
        super();
    }

    /**
     * Method playPhase disables everything except the entrance hall students, adding the respective command on the mouse clicked event.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();
        viewGUI.enableEntrance(GameCommand.MOVE_STUDENT.getGUIHandler(viewGUI));
        if (getGameCommands().contains(GameCommand.CHOOSE_CHARACTER)) {
            viewGUI.enableChooseCharacter(GameCommand.CHOOSE_CHARACTER.getGUIHandler(viewGUI));
        }
    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Move Student Phase".
     */
    @Override
    public String toString() {
        return "Move Student Phase";
    }
}
