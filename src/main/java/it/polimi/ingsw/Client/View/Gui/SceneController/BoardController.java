package it.polimi.ingsw.Client.View.Gui.SceneController;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Util.AssistantCard;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class BoardController implements SceneController {
    AbstractView view;

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
    public void updateMessage(String message) {

    }
    @Override
    public void setViewGUI(ViewGUI viewGUI) {
        view = viewGUI;
    }

    @Override
    public void hideEverything() {

    }

    @Override
    public Node getElementById(String id) {
        return null;
    }
}
