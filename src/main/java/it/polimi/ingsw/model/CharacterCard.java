package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;

public interface CharacterCard {
    void play(ExpertGame game) throws GameException, EndGameException;
    byte getCost();
    int getId();
    boolean canPlay(int nInput);
}
