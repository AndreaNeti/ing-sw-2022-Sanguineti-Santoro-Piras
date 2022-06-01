package it.polimi.ingsw.Client.View.Gui.SceneController;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.model.AssistantCard;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class MenuController implements SceneController {
    AbstractView view;
    @FXML
    Group main;
    @FXML
    ToggleGroup gameType;
    @FXML
    Button connectButton;

    @FXML
    void initialize() {
        hideEverything();
    }

    public void hideEverything() {
        for (Node child : main.getChildren()) {
            child.setVisible(false);
        }
    }

    public Node getElementById(String id) {
        return main.lookup(id);
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
    }

    @Override
    public void update(GameComponentClient gameComponent) {

    }

    @Override
    public void update(IslandClient island) {

    }

    @Override
    public void update(ArrayList<IslandClient> islands) {

    }

    @Override
    public void update(HouseColor houseColor, Byte towerLefts) {

    }

    @Override
    public void update(Color color, Wizard wizard) {

    }

    @Override
    public void update(String currentPlayer, boolean isMyTurn) {

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

    @Override
    public void update() {
    }

    @Override
    public void setView(AbstractView abstractView) {
        this.view = abstractView;
    }
}
