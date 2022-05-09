package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;

public interface CharacterCard {
    void play(ExpertGame game) throws GameException, EndGameException;
    byte getCost();
    byte getCharId();
    boolean canPlay(int nInput);
}
