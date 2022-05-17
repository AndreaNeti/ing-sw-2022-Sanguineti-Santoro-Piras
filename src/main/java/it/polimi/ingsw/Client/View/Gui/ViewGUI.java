package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.ClientPhaseView;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;

import java.util.ArrayList;

public class ViewGUI extends AbstractView {
    public ViewGUI(ControllerClient controllerClient) {
        super(controllerClient);
    }

    @Override
    public void setPhaseInView(ClientPhaseView clientPhaseView) {

    }

    @Override
    public void start() throws InterruptedException {

    }

    @Override
    public void setQuit() {

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
    public void update(Wizard[] professors) {

    }

    @Override
    public void error(String e) {

    }

    @Override
    public void ok() {

    }

    @Override
    public void update(String currentPlayer) {

    }

    @Override
    public void updateMembers(int membersLeftToStart) {

    }


    @Override
    public void updateCardPlayed(Byte playedCard) {

    }
}
