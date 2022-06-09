package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;

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

    Color getIgnoredColorInfluence();

    boolean isExtraSteps();

    Byte getNewCoinsLeft();

    void addListener(GameClientListener listener);
}
