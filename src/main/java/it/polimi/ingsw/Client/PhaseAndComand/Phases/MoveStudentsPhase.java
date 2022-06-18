package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import javafx.scene.Node;
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
        for (int i = 0; i < viewGUI.getModel().getCurrentPlayer().getEntranceHall().howManyStudents(); i++) {
            Node student = entranceHall.getChildren().get(i);
            sceneController.enableNode(student);
            student.setOnMouseClicked(GameCommand.MOVE_STUDENT.getGUIHandler(viewGUI));
        }
        if (getGameCommand().contains(GameCommand.CHOOSE_CHARACTER)) {
        //TODO
        }
    }

    @Override
    public String toString() {
        return "Move Student Phase";
    }
}
