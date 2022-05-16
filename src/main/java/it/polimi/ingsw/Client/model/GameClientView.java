package it.polimi.ingsw.Client.model;

import java.util.ArrayList;

public interface GameClientView {
    PlayerClient getCurrentPlayer();
    ArrayList<GameComponentClient> getClouds();
}
