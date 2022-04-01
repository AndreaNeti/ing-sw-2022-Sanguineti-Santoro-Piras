package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ExpertGame;

public interface CharacterCard {
    void play(ExpertGame game);
    byte getCost();
    int getId();
    boolean canPlay(int nInput);
}
