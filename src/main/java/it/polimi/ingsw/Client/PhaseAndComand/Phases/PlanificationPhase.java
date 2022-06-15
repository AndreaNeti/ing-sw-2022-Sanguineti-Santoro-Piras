package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Game;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.security.auth.login.AccountNotFoundException;


public class PlanificationPhase extends ClientPhase {
    public PlanificationPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();
        System.out.println("#assistantCard" + viewGUI.getModel().getMyWizard());
        AnchorPane assistantCardPane = (AnchorPane) sceneController.getElementById("#assistantCard" + viewGUI.getModel().getMyWizard());
        AnchorPane deck = (AnchorPane) assistantCardPane.getChildren().get(0);
        sceneController.getElementById("#mainBoard").setDisable(false);
        deck.setDisable(false);
        deck.setOnMouseClicked(null);
        deck.setStyle("-fx-border-color: #f5f50a");
    }

    @Override
    public String toString() {
        return "Planification Phase";
    }
}
