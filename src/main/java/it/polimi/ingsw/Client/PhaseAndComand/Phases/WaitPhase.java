package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

public class WaitPhase extends ClientPhase {
    public WaitPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getSceneController();
        sceneController.hideEverything();
        sceneController.getElementById("#chat").setVisible(true);
    }

    @Override
    public String toString() {
        return "Wait phase";
    }
}
