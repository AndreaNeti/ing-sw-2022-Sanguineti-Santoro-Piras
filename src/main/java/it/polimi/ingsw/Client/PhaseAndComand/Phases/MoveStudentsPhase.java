package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import javafx.scene.layout.AnchorPane;

public class MoveStudentsPhase extends ClientPhase {
    public MoveStudentsPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();
        //entranceHall is the second children
        AnchorPane entranceHall = (AnchorPane) ((AnchorPane) sceneController.getElementById("#mainBoard")).getChildren().get(2);
        sceneController.enableNode(entranceHall);
        entranceHall.setOnMouseClicked(GameCommand.MOVE_STUDENT.getGUIHandler(viewGUI));

    }

    @Override
    public String toString() {
        return "Move Student Phase";
    }
}
