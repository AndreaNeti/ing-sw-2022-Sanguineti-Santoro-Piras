package it.polimi.ingsw.model.character;

interface Character {
    void play();
    byte getCost();
    void reset();
    void setInput(int input);
    int getId();
    boolean canPlay();
}
