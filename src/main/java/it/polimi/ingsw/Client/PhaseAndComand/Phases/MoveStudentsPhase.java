package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

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
        //entranceHall is the second children
        AnchorPane entranceHall = (AnchorPane) ((AnchorPane) sceneController.getElementById("#mainBoard")).getChildren().get(2);
        for (int i = 0; i < viewGUI.getModel().getCurrentPlayer().getEntranceHall().howManyStudents(); i++) {
            Node student = entranceHall.getChildren().get(i);
            sceneController.enableNode(student);
            student.setOnMouseClicked(GameCommand.MOVE_STUDENT.getGUIHandler(viewGUI));
        }
        if (getGameCommands().contains(GameCommand.CHOOSE_CHARACTER)) {
        //TODO
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
