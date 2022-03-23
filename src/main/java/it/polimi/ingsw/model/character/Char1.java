package it.polimi.ingsw.model.character;

public class Char1 implements CharacterCard {
    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public void reset() {}

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public boolean canPlay() {
        return true;
    }
}