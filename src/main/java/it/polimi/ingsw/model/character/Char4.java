package it.polimi.ingsw.model.character;

public class Char4 implements Character {

    private byte nInput = 0;
    private byte prohibitionsLeft = 4;

    @Override
    public void play() {
        prohibitionsLeft--;
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setInput(int input) {

    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public boolean canPlay() {
        return false;
    }
}