package it.polimi.ingsw.model.character;

public class Char8 implements CharacterCard {
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
        return 8;
    }

    @Override
    public boolean canPlay() {
        return true;
    }
}
