package it.polimi.ingsw.model.character;

public class Char2 implements Character {

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
        return false;
    }
}