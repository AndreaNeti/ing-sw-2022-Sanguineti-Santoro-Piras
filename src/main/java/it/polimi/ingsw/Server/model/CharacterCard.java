package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

public interface CharacterCard {
    void play(CharacterCardGame game) throws GameException, EndGameException;
    byte getCost();
    byte getCharId();
    boolean canPlay(int nInput);
}
