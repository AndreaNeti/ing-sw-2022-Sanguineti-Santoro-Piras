package it.polimi.ingsw.client.view.Gui.SceneController;

import it.polimi.ingsw.client.model.GameComponentClient;
import it.polimi.ingsw.client.model.IslandClient;
import it.polimi.ingsw.client.model.PlayerClient;
import it.polimi.ingsw.client.model.TeamClient;
import it.polimi.ingsw.client.phaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.client.view.Gui.GuiFX;
import it.polimi.ingsw.client.view.Gui.ViewGUI;
import it.polimi.ingsw.utils.*;
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
    public VBox playerLobby;
    public Button sendButton;
    private ObservableList<String> observableListChat;
    private ObservableList<String> playersInLobbyList;

    /**
     * Method initialize creates and sets elements in the scene
     */
    private void initialize() {
        hideEverything();
        chatButton.setOnAction(actionEvent -> chat.setVisible(!chat.isVisible()));
        //create the chat
        observableListChat = FXCollections.observableArrayList();
        observableListChat.addAll(viewGUI.getChat());
        ListView<String> listView = new ListView<>(observableListChat);
        listView.prefWidthProperty().bind(paneForChat.widthProperty());
        paneForChat.getChildren().add(listView);
        chat.toFront();
        chat.setVisible(false);

        Pane playerLobbyPane = (Pane) ((VBox) playerLobby.getChildren().get(0)).getChildren().get(1);
        playersInLobbyList = FXCollections.observableArrayList();
        ListView<String> lobbyListView = new ListView<>(playersInLobbyList);
        playerLobby.setVisible(false);
        lobbyListView.prefHeightProperty().bind(playerLobbyPane.heightProperty());
        playerLobbyPane.getChildren().add(lobbyListView);
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
        playerLobby.setVisible(false);
    }

    @Override
    public void enableNode(Node node) {
    }

    @Override
    public void enableNode(Node node, boolean addVisibility) {
    }

    @Override
    public void selectNode(Node node) {

    }

    @Override
    public void disableEverything() {
        hideEverything();
    }

    @Override
    public Node getElementById(String id) {

        // toggle group are not a node, but I'll make them return the selected radio button
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
    public void updateProhibitions(Byte newProhibitions) {

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
    public void updateDeletedIsland(IslandClient island, IslandClient idIslandWInner) {

    }

    @Override
    public void updateTowerLeft(HouseColor houseColor, Byte towerLefts) {

    }

    @Override
    public void updateProfessor(Color color, Wizard wizard) {

    }

    @Override
    public void updateMembers(int membersLeftToStart, PlayerClient playerJoined) {
        Platform.runLater(() -> {
            if (membersLeftToStart == 0) {
                hideEverything();
                chat.setVisible(false);
                chatButton.setVisible(false);
            } else {
                playersInLobbyList.add(playerJoined.getNickName());
            }
        });
    }

    @Override
    public void updateMatchInfo(MatchType matchType, MatchConstants constants, List<TeamClient> teams) {
        Platform.runLater(() -> {
            playersInLobbyList.clear();
            for (TeamClient t : teams)
                playersInLobbyList.addAll(t.getPlayers().stream().map(PlayerClient::getNickName).toList());
            playerLobby.setVisible(true);
        });
    }

    @Override
    public void updateError(String error) {
        GuiFX.showError("Server error", error, "Error");
    }

    @Override
    public void updateCurrentPlayer(byte newCurrentPlayer) {

    }

    @Override
    public void updateCardPlayed(AssistantCard playedCard, Wizard wizard) {

    }

    @Override
    public void updateIgnoredColor(Color color) {
    }

    @Override
    public void updateExtraSteps(boolean extraSteps) {
    }

    @Override
    public void updateCharacter(byte charId) {

    }

    @Override
    public void updateCoins(Integer coins) {

    }

    @Override
    public void updateCoins(Wizard wizard, Integer coins) {

    }

    @Override
    public void setWinners(List<HouseColor> winners) {

    }
}
