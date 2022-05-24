package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;

import java.util.ArrayList;
import java.util.List;

public interface GameClientListener {
    void updateMotherNature(Byte motherNaturePosition);

    void update(GameComponentClient gameComponent);

    void update(IslandClient island);

    void update(ArrayList<IslandClient> islands);

    void update(HouseColor houseColor, Byte towerLefts);

    void update(Color color, Wizard wizard);

//    void error(String e);
//
//    void ok();

    void update(String currentPlayer, boolean isMyTurn);

    void updateMembers(int membersLeftToStart, String nickPlayerJoined);

    //TODO update withcoins, character
    void updateCardPlayed(Byte playedCard);

    void updateIgnoredColor(Color color);

    void updateCharacter(List<CharacterCardClient> characters);

    void updateCoins(Byte coins);

    void setWinners(List<HouseColor> winners);

    void update();
    //void setModel(GameClientView model);

}
