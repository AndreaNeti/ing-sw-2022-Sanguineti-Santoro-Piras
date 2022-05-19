package it.polimi.ingsw.Client.model;

import java.util.ArrayList;
import java.util.List;

public interface GameClientView {
    PlayerClient getCurrentPlayer();

    ArrayList<GameComponentClient> getClouds();

    ArrayList<IslandClient> getIslands();
    List<PlayerClient> getPlayers();
}
