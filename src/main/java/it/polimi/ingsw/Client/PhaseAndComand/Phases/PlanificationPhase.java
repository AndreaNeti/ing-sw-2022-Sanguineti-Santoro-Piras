package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Util.AssistantCard;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

        //add to my image of wizard the handler that show all the playCharacter
        AnchorPane assistantCardsBox = (AnchorPane) sceneController.getElementById("#assistantCardsBox");
        HBox assistantCardPane = (HBox) sceneController.getElementById("#assistantCard" + viewGUI.getModel().getMyWizard());
        AnchorPane deck = (AnchorPane) assistantCardPane.getChildren().get(1);
        sceneController.enableNode(deck);
        deck.setOnMouseClicked(mouseEvent -> assistantCardsBox.setVisible(!assistantCardsBox.isVisible()));

        HBox box = (HBox) ((VBox) assistantCardsBox.getChildren().get(0)).getChildren().get(1);
        box.getChildren().clear();
        for (AssistantCard card : viewGUI.getModel().getCurrentPlayer().getAssistantCards()) {
            ImageView cardImage = new ImageView();
            cardImage.setImage(new Image("Graphical_Assets/AssistantCard/" + card.value() + ".png"));
            cardImage.getStyleClass().add("card");
            cardImage.setPreserveRatio(true);
            cardImage.fitWidthProperty().bind(assistantCardsBox.widthProperty().subtract(2 * assistantCardsBox.getPadding().getLeft()).divide(10).subtract(20));
            cardImage.getProperties().put("cardValue", card);
            HBox.setMargin(cardImage, new Insets(20, 10, 10, 10));
            box.getChildren().add(cardImage);
            sceneController.enableNode(cardImage);
            cardImage.addEventHandler(MouseEvent.MOUSE_CLICKED, GameCommand.PLAY_CARD.getGUIHandler(viewGUI));
        }
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
