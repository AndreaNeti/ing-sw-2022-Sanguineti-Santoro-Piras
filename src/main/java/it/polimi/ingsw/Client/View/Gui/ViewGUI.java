package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.MatchType;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ViewGUI extends AbstractView {
    private ClientPhase phaseToExecute;

    public ViewGUI(ControllerClient controllerClient) {
        super(controllerClient);
    }

    @Override
    protected synchronized void setPhaseInView(ClientPhase clientPhase, boolean forceImmediateExecution) {
        phaseToExecute = clientPhase;
        //if it's not in a game should load the menu scene otherwise the board scene
        if (getModel() == null) {
            Platform.runLater(GuiFX::goToMenuScene);
        } else {
            Platform.runLater(GuiFX::goToBoardScene);
        }
        Platform.runLater(() -> phaseToExecute.playPhase(this));
    }

    @Override
    public void setQuit(boolean forceImmediateExecution) {
        super.setQuit(forceImmediateExecution);
        if (canQuit()) Platform.exit();
    }

    public void enableEntrance(EventHandler<MouseEvent> event) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        //entranceHall is the second children
        AnchorPane entranceHall = (AnchorPane) ((AnchorPane) sceneController.getElementById("#mainBoard")).getChildren().get(2);
        for (int i = 0; i < getModel().getCurrentPlayer().getEntranceHall().howManyStudents(); i++) {
            Node student = entranceHall.getChildren().get(i);
            sceneController.enableNode(student);
            student.setOnMouseClicked(event);
        }
    }

    public void enableIslands(EventHandler<MouseEvent> event) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        for (byte id = (byte) (2 * MatchType.MAX_PLAYERS); id < 2 * MatchType.MAX_PLAYERS + 12; id++) {
            Node island = sceneController.getElementById("#" + id);
            sceneController.enableNode(island);
            island.setOnMouseClicked(event);
        }
    }

    public void enableChooseCharacter(EventHandler<MouseEvent> event) {

        SceneController sceneController = GuiFX.getActiveSceneController();
        for (Node n : ((HBox) sceneController.getElementById("#characters")).getChildren()) {
            AnchorPane singleChar = (AnchorPane) n;
            Button b = (Button) singleChar.getChildren().get(2);
            sceneController.enableNode(b, true);
            b.setOnMouseClicked(event);
        }
    }

    public void enableStudentsOnCharacter(int idChar, EventHandler<MouseEvent> event) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        AnchorPane paneStudent = (AnchorPane) ((AnchorPane) sceneController.getElementById("#" + idChar)).getChildren().get(1);
        for (Node student : paneStudent.getChildren()) {
            Node imageToClick = ((AnchorPane) student).getChildren().get(0);
            imageToClick.setOnMouseClicked(event);
            sceneController.enableNode(imageToClick);
        }
    }

    public void enableColorBox(EventHandler<MouseEvent> event) {

        SceneController sceneController = GuiFX.getActiveSceneController();
        //Enabling color box
        HBox colorBox = (HBox) sceneController.getElementById("#colorBox");
        sceneController.enableNode(colorBox, true);
        for (Node n : colorBox.getChildren()) {
            n.setOnMouseClicked(event);
        }
    }

    public void enableStudentsLunchHall(EventHandler<MouseEvent> event) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        //lunchHall is the third child of lunchHall
        VBox lunchHall = (VBox) ((AnchorPane) sceneController.getElementById("#mainBoard")).getChildren().get(3);
        lunchHall.setOnMouseClicked(null);
        sceneController.enableNode(lunchHall);
        for (Node n : lunchHall.getChildren()) {
            sceneController.enableNode(n);
            n.setOnMouseClicked(event);
        }
    }

    public void updateAssistantBox() {
        updateAssistantBox(false);
    }

    public void updateAssistantBox(boolean addHandler) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        AnchorPane assistantCardsBox = (AnchorPane) sceneController.getElementById("#assistantCardsBox");
        HBox box = (HBox) ((VBox) assistantCardsBox.getChildren().get(0)).getChildren().get(1);
        box.getChildren().clear();
        for (AssistantCard card : this.getModel().getPlayers().get(getModel().getMyWizard().ordinal()).getAssistantCards()) {
            ImageView cardImage = new ImageView();
            cardImage.setImage(new Image("Graphical_Assets/AssistantCard/" + card.value() + ".png"));
            cardImage.getStyleClass().add("card");
            cardImage.setPreserveRatio(true);
            cardImage.fitWidthProperty().bind(assistantCardsBox.widthProperty().subtract(2 * assistantCardsBox.getPadding().getLeft()).divide(10).subtract(20));
            cardImage.getProperties().put("cardValue", card);
            HBox.setMargin(cardImage, new Insets(20, 10, 10, 10));
            box.getChildren().add(cardImage);
            if (addHandler) {
                sceneController.enableNode(cardImage);
                cardImage.setOnMouseClicked(GameCommand.PLAY_CARD.getGUIHandler(this));
            }
        }
    }
}
