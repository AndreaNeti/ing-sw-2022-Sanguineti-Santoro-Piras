package it.polimi.ingsw.model.character;

public interface CharacterCard {
    void play();
    byte getCost();
    void reset();
    void setInput(int input);
    int getId();
    boolean canPlay();
}
