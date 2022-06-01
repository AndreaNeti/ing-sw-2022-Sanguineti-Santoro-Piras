package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import javafx.application.Platform;

public class ViewGUI extends AbstractView {
    private SceneController sceneController;
    private ClientPhase phaseToExecute;

    public ViewGUI(ControllerClient controllerClient) {
        super(controllerClient);
    }

    @Override
    public synchronized void setPhaseInView(ClientPhase clientPhase, boolean forceImmediateExecution) {
        phaseToExecute = clientPhase;
        //if it's not in a game should load the menu scene otherwise the board scene
        if (getModel() == null) {
            Platform.runLater(GuiFX::goToMenuScene);
        } else {
            Platform.runLater(GuiFX::goToBoardScene);
        }
        Platform.runLater(() -> {
            sceneController = GuiFX.getSceneController();
            phaseToExecute.playPhase(this, sceneController);
        });
    }


    @Override
    public void setQuit(boolean forceImmediateExecution) {

    }
}
