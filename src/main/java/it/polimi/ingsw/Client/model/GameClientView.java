package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.network.toClientMessage.MatchInfo;

import java.util.ArrayList;
import java.util.List;

public interface GameClientView {
    PlayerClient getCurrentPlayer();
    List<CharacterCardClient> getCharacters();
    ArrayList<GameComponentClient> getClouds();

    ArrayList<IslandClient> getIslands();
    List<TeamClient> getTeams();
    CharacterCardClient getCurrentCharacterCard();
    MatchType getMatchType();

    PlayerClient getPlayer(int index);
}
