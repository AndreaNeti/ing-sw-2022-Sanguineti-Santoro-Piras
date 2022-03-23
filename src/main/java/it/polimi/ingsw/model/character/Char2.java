package it.polimi.ingsw.model.character;

public class Char2 implements CharacterCard {

    private byte nInput = 0;

    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public void reset() {
        nInput = 0;
    }

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public boolean canPlay() {
        return nInput == 1;
    }
}