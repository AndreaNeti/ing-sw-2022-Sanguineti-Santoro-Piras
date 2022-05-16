package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;

import java.util.ArrayList;

public interface GameClientListener {



    void updateMotherNature(Byte motherNaturePosition);
    void update(GameComponentClient gameComponent);
    void update(IslandClient island);
    void update(ArrayList<IslandClient> islands);
    void update(HouseColor houseColor,Byte towerLefts);
    void update(Wizard[] professors);
    void error(String e);
    void ok();
    void update(String currentPlayer);
    void updateMembers(int membersLeftToStart);
    //TODO update withcoins, character
    void update(GamePhase gamePhase);
    void updateCardPlayed(Byte playedCard);
    void setView(GamePhaseView gamePhaseView);

}
