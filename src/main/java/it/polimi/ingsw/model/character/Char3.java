package it.polimi.ingsw.model.character;

public class Char3 implements CharacterCard {
    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public void reset() {}

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public boolean canPlay() {
        return true;
    }
}