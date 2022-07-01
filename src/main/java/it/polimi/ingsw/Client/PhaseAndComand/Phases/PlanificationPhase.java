package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * PlanificationPhase class represents the game phase in which the client can play an assistant card.
 */
public class PlanificationPhase extends ClientPhase {

    /**
     * Constructor PlanificationPhase creates a new instance of PlanificationPhase.
     */
    public PlanificationPhase() {
        super();
    }

    /**
     * Method playPhase disables everything except the assistant cards deck, making it show the cards available when clicked. <br>
     * If clicked, the assistant cards are shown, adding the respective command on the mouse clicked event.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();

        HBox assistantCardsBox = (HBox) sceneController.getElementById("#assistantCardsBox");
        assistantCardsBox.setVisible(true);
        viewGUI.updateAssistantBox();
        // this is the label of the vbox


    }

    /**
     * Method toString returns the name of the phase.
     *
     * @return {@code String} - "Planification Phase".
     */
    @Override
    public String toString() {
        return "Planification Phase";
    }
}
