package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;

import java.util.ArrayList;

public interface GameClientListener {
    void update(Byte motherNaturePosition);
    void update(GameComponentClient gameComponent);
    void update(IslandClient island);
    void update(ArrayList<IslandClient> islands);
    void update(HouseColor houseColor,Byte towerLefts);
    void update(Wizard[] professors);
    void update(Integer playedCard);
    void error(String e);
    void ok();
    void update(PlayerClient currentPlayer);
    //TODO update with playedCard, coins, character

}
