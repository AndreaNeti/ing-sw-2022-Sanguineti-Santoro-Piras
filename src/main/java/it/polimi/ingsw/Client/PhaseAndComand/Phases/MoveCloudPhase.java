package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.GameComponentClient;
import javafx.scene.Node;

public class MoveCloudPhase extends ClientPhase {
    public MoveCloudPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();
        for (GameComponentClient cloud : viewGUI.getModel().getClouds()) {
            Node cloudCliclable = sceneController.getElementById("#" + cloud.getId());
            sceneController.enableNode(cloudCliclable);
            cloudCliclable.setOnMouseClicked(GameCommand.MOVE_FROM_CLOUD.getGUIHandler(viewGUI));
        }
    }

    @Override
    public String toString() {
        return "Cloud phase";
    }
}
