package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Server.model.HouseColor;
import it.polimi.ingsw.Server.model.Island;
import it.polimi.ingsw.Server.model.Wizard;
import it.polimi.ingsw.exceptions.GameException;

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
