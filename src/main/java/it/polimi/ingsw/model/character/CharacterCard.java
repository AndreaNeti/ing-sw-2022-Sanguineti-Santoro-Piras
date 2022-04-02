package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.model.ExpertGame;

public interface CharacterCard {
    void play(ExpertGame game) throws GameException;
    byte getCost();
    int getId();
    boolean canPlay(int nInput);
}
