package it.polimi.ingsw.Client.View.Gui.SceneController;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Util.AssistantCard;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    @FXML
    AnchorPane root;
    ViewGUI viewGUI;
    @FXML
    Group mainGroup;
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
        chatButton.setOnAction(actionEvent ->

                switchChat());
        //create the chat
        observableListChat = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(observableListChat);
        listView.prefWidthProperty().bind(paneForChat.widthProperty());
        listView.prefHeightProperty().bind(paneForChat.heightProperty());
        paneForChat.getChildren().add(listView);
        chat.toFront();
        chat.setVisible(false);
        //add event handler
        connectButton.setOnAction(GameCommand.CONNECT_SERVER.getGUIHandler(viewGUI));
        nickNameButton.setOnAction(GameCommand.SET_NICKNAME.getGUIHandler(viewGUI));
        createButton.setOnAction(GameCommand.CREATE_MATCH.getGUIHandler(viewGUI));
        joinIdButton.setOnAction(GameCommand.JOIN_MATCH_BY_ID.getGUIHandler(viewGUI));
        joinMatchTypeButton.setOnAction(GameCommand.JOIN_MATCH_BY_TYPE.getGUIHandler(viewGUI));
        sendButton.setOnAction(GameCommand.TEXT_MESSAGE.getGUIHandler(viewGUI));
    }

    @Override
    public void hideEverything() {
        for (Node child : mainGroup.getChildren()) {
            child.setVisible(false);
        }
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

    public void switchChat() {
        chat.setVisible(!chat.isVisible());
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

    public void updateGameComponent(String currentPlayer, boolean isMyTurn) {

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