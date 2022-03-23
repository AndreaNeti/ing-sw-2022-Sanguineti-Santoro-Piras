package it.polimi.ingsw.model.character;

public class Char5 implements CharacterCard {
    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public void reset() {}

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public boolean canPlay() {
        return true;
    }
}
