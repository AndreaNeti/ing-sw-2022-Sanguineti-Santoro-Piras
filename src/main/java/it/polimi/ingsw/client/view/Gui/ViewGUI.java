package it.polimi.ingsw.client.view.Gui;

import it.polimi.ingsw.client.controller.ControllerClient;
import it.polimi.ingsw.client.model.PlayerClient;
import it.polimi.ingsw.client.phaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.client.phaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.client.view.AbstractView;
import it.polimi.ingsw.client.view.Gui.SceneController.SceneController;
import it.polimi.ingsw.utils.AssistantCard;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.MatchType;
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

/**
 * ViewCLI class represents the GUI view that allows the user to interact with the client. <br>
 * It contains methods to modify the graphical components and interact with them in order to obtain inputs from the user.
 */
public class ViewGUI extends AbstractView {
    private ClientPhase phaseToExecute;

    /**
     * Constructor ViewGUI creates a new instance of ViewGUI.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client's controller.
     */
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

    /**
     * Method enableEntrance enables the entrance hall, adding the provided event handler to its students on the mouse clicked event.
     *
     * @param event of type {@code EventHandler}<{@code MouseEvent}> - function that will be executed when a student is clicked.
     */
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

    /**
     * Method enableIslands enables the islands, adding the provided event handler to them on the mouse clicked event.
     *
     * @param event of type {@code EventHandler}<{@code MouseEvent}> - function that will be executed when an island is clicked.
     */
    public void enableIslands(EventHandler<MouseEvent> event) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        for (byte id = (byte) (2 * MatchType.MAX_PLAYERS); id < 2 * MatchType.MAX_PLAYERS + 12; id++) {
            Node island = sceneController.getElementById("#" + id);
            sceneController.enableNode(island);
            island.setOnMouseClicked(event);
        }
    }

    /**
     * Method enableChooseCharacter enables the buttons to choose the character cards, adding the provided event handler to them on the mouse clicked event.
     *
     * @param event of type {@code EventHandler}<{@code MouseEvent}> - function that will be executed when the button to choose a character card is clicked.
     */
    public void enableChooseCharacter(EventHandler<MouseEvent> event) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        for (Node n : ((HBox) sceneController.getElementById("#characters")).getChildren()) {
            AnchorPane singleChar = (AnchorPane) n;
            Button b = (Button) singleChar.getChildren().get(2);
            sceneController.enableNode(b, true);
            b.setOnMouseClicked(event);
        }
    }

    /**
     * Method enableStudentsOnCharacter enables the students of a character card, adding the provided event handler to them on the mouse clicked event.
     *
     * @param event of type {@code EventHandler}<{@code MouseEvent}> - function that will be executed when a student is clicked.
     */
    public void enableStudentsOnCharacter(int idChar, EventHandler<MouseEvent> event) {
        SceneController sceneController = GuiFX.getActiveSceneController();
        AnchorPane paneStudent = (AnchorPane) ((AnchorPane) sceneController.getElementById("#" + idChar)).getChildren().get(1);
        for (Node student : paneStudent.getChildren()) {
            Node imageToClick = ((AnchorPane) student).getChildren().get(0);
            imageToClick.setOnMouseClicked(event);
            sceneController.enableNode(imageToClick);
        }
    }

    /**
     * Method enableColorBox enables the box to select colors, adding the provided event handler to them on the mouse clicked event.
     *
     * @param event of type {@code EventHandler}<{@code MouseEvent}> - function that will be executed when a color is clicked.
     */
    public void enableColorBox(EventHandler<MouseEvent> event) {

        SceneController sceneController = GuiFX.getActiveSceneController();
        //Enabling color box
        HBox colorBox = (HBox) sceneController.getElementById("#colorBox");
        sceneController.enableNode(colorBox, true);
        for (Node n : colorBox.getChildren()) {
            n.setOnMouseClicked(event);
        }
    }

    /**
     * Method enableEntrance enables the students of the entrance hall, adding the provided event handler to them on the mouse clicked event.
     *
     * @param event of type {@code EventHandler}<{@code MouseEvent}> - function that will be executed when a student is clicked.
     */
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

    /**
     * Method updateAssistantBox updates the assistant box of the client's player and makes it visible. <br>
     */
    public void updateAssistantBox() {
        updateAssistantBox(this.getModel().getPlayers().get(getModel().getMyWizard().ordinal()));
    }


    /**
     * Method updateAssistantBox updates the assistant box of the provided player and makes it visible. <br>
     * If the player provided is equal to the client's player and is currently its turn the event handler on mouse clicked is added on each card available.
     *
     * @param playerClient of type {@link PlayerClient} - instance of the player of which the assistant box is shown.
     */
    public void updateAssistantBox(PlayerClient playerClient) {
        SceneController sceneController = GuiFX.getActiveSceneController();

        HBox assistantCardsBox = (HBox) sceneController.getElementById("#assistantCardsBox");
        VBox content = (VBox) assistantCardsBox.getChildren().get(0);
        HBox box = (HBox) (content).getChildren().get(1);
        box.getChildren().clear();
        boolean condition = getModel().getCurrentPlayer().equals(playerClient) && getCurrentPhase().equals(GamePhase.PLANIFICATION_PHASE);
        if (condition) {
            sceneController.disableEverything();
            content.getChildren().get(0).setVisible(true);
        } else {
            content.getChildren().get(0).setVisible(false);
        }
        for (AssistantCard card : playerClient.getAssistantCards()) {
            ImageView cardImage = new ImageView();
            cardImage.setImage(new Image("Graphical_Assets/AssistantCard/" + card.value() + ".png"));
            cardImage.getStyleClass().add("card");
            cardImage.setPreserveRatio(true);
            cardImage.fitWidthProperty().bind(assistantCardsBox.widthProperty().subtract(2 * assistantCardsBox.getPadding().getLeft()).divide(10).subtract(20));
            cardImage.getProperties().put("cardValue", card);
            HBox.setMargin(cardImage, new Insets(20, 10, 10, 10));
            box.getChildren().add(cardImage);
            if (condition) {
                sceneController.enableNode(cardImage);
                cardImage.setOnMouseClicked(GameCommand.PLAY_CARD.getGUIHandler(this));
            }
        }
    }
}
