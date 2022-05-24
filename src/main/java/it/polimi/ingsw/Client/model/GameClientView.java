package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.network.toClientMessage.MatchInfo;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;

import java.util.ArrayList;
import java.util.List;

public interface GameClientView {
    PlayerClient getCurrentPlayer();
    List<CharacterCardClient> getCharacters();
    ArrayList<GameComponentClient> getClouds();

    ArrayList<IslandClient> getIslands();
    List<TeamClient> getTeams();
    CharacterCardClient getCurrentCharacterCard();
    byte getMotherNaturePosition();

    boolean isExpert();

    Wizard[] getProfessors();

    Byte getNewProhibitionsLeft();

    MatchConstants getMatchConstants();

    Wizard getMyWizard();

    byte getCoinsPlayer(byte i);
    MatchType getMatchType();
    PlayerClient getPlayer(int index);
    List<PlayerClient> getPlayers();
}
