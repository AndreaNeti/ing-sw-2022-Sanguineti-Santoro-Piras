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


public class PlanificationPhase extends ClientPhase {
    public PlanificationPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        sceneController.disableEverything();

        //add to my image of wizard the handler that show all the playCharacter
        AnchorPane assistantCardsBox = (AnchorPane) sceneController.getElementById("#assistantCardsBox");
        AnchorPane assistantCardPane = (AnchorPane) sceneController.getElementById("#assistantCard" + viewGUI.getModel().getMyWizard());
        AnchorPane deck = (AnchorPane) assistantCardPane.getChildren().get(0);
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

    @Override
    public String toString() {
        return "Planification Phase";
    }
}
