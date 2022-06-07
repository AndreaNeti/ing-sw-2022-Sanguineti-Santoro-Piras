package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

public class SelectMatchPhase extends ClientPhase {
    public SelectMatchPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getSceneController();
        sceneController.hideEverything();
        sceneController.getElementById("#selectMatch").setVisible(true);
    }

    public String toString() {
        return "Select match phase";
    }
}
