package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;

import java.util.List;

public interface GameClientListener {
    void updateMotherNature(Byte motherNaturePosition);

    void updateGameComponent(GameComponentClient gameComponent);

    void updateGameComponent(IslandClient island);

    void updateDeletedIsland(IslandClient island);

    void updateTowerLeft(HouseColor houseColor, Byte towerLefts);

    void updateProfessor(Color color, Wizard wizard);


    void updateMembers(int membersLeftToStart, String nickPlayerJoined);

    void updateCardPlayed(AssistantCard playedCard);

    void updateIgnoredColor(Color color);

    void updateExtraSteps(boolean extraSteps);

    void updateCharacter(List<CharacterCardClient> characters);

    void updateCoins(Byte coins);

    void setWinners(List<HouseColor> winners);

    void updateMessage(String message);

}
