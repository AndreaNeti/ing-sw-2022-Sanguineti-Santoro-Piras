package it.polimi.ingsw.Client.View.Gui.SceneController;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MenuController implements SceneController {
    public Button createButton;
    public Button joinIdButton;
    public Button joinMatchTypeButton;
    public ToggleGroup player;
    public ToggleGroup gameType;
    public Button quitButton;
    @FXML
    AnchorPane root;
    ViewGUI viewGUI;
    @FXML
    AnchorPane mainGroup;
    @FXML
    Button connectButton;
    @FXML
    Button chatButton;
    @FXML
    Button nickNameButton;
    public VBox chat;
    public Pane paneForChat;
    public Button sendButton;
    private ObservableList<String> observableListChat;


    private void initialize() {

        hideEverything();
        chatButton.setOnAction(actionEvent -> chat.setVisible(!chat.isVisible()));
        //create the chat
        observableListChat = FXCollections.observableArrayList();
        observableListChat.addAll(viewGUI.getChat());
        ListView<String> listView = new ListView<>(observableListChat);
        listView.prefWidthProperty().bind(paneForChat.widthProperty());
        listView.prefHeightProperty().bind(paneForChat.heightProperty());
        paneForChat.getChildren().add(listView);
        chat.toFront();
        chat.setVisible(false);
        //add event handler
        connectButton.setOnMouseClicked(GameCommand.CONNECT_SERVER.getGUIHandler(viewGUI));
        nickNameButton.setOnMouseClicked(GameCommand.SET_NICKNAME.getGUIHandler(viewGUI));
        createButton.setOnMouseClicked(GameCommand.CREATE_MATCH.getGUIHandler(viewGUI));
        joinIdButton.setOnMouseClicked(GameCommand.JOIN_MATCH_BY_ID.getGUIHandler(viewGUI));
        joinMatchTypeButton.setOnMouseClicked(GameCommand.JOIN_MATCH_BY_TYPE.getGUIHandler(viewGUI));
        sendButton.setOnMouseClicked(GameCommand.TEXT_MESSAGE.getGUIHandler(viewGUI));
        quitButton.setOnMouseClicked(GameCommand.QUIT.getGUIHandler(viewGUI));
    }

    @Override
    public void hideEverything() {
        for (Node child : mainGroup.getChildren()) {
            child.setVisible(false);
        }
    }

    @Override
    public void enableNode(Node node) {
        node.setVisible(true);
    }

    @Override
    public void disableEverything() {
        hideEverything();
    }

    @Override
    public Node getElementById(String id) {

        //toggle group are not a node but i'll make them return the selected radio button
        if (id.equals("#gameType")) {
            return (RadioButton) gameType.getSelectedToggle();
        }
        if (id.equals("#player")) {
            return (RadioButton) player.getSelectedToggle();
        }
        return root.lookup(id);
    }


    @Override
    public void updateMessage(String message) {
        Platform.runLater(() -> {
            if (observableListChat.size() > 15) {
                observableListChat.remove(0);
            }
            observableListChat.add(message);
        });
    }

    @Override
    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
        this.initialize();
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
    }

    @Override
    public void updateGameComponent(GameComponentClient gameComponent) {

    }

    @Override
    public void updateGameComponent(IslandClient island) {

    }

    @Override
    public void updateDeletedIsland(IslandClient island) {

    }

    @Override
    public void updateTowerLeft(HouseColor houseColor, Byte towerLefts) {

    }

    @Override
    public void updateProfessor(Color color, Wizard wizard) {

    }

    @Override
    public void updateMembers(int membersLeftToStart, String nickPlayerJoined) {
    }

    @Override
    public void updateCardPlayed(AssistantCard playedCard) {

    }

    @Override
    public void updateIgnoredColor(Color color) {
    }

    @Override
    public void updateExtraSteps(boolean extraSteps) {
    }

    @Override
    public void updateCharacter(List<CharacterCardClient> characters) {

    }

    @Override
    public void updateCoins(Byte coins) {

    }

    @Override
    public void setWinners(List<HouseColor> winners) {

    }
}
