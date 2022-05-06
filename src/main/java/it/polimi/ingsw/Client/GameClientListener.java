package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Server.model.Island;
import it.polimi.ingsw.Server.model.Wizard;

public interface GameClientListener {
    void update(Byte motherNaturePosition);
    void update(GameComponentClient gameComponent);
    void update(IslandClient island);
    void update(Integer towerLefts);
    void update(Wizard[] professors);
    //TODO update with playedCard, coins, character

}
