package it.polimi.ingsw.model.character;

public class Char4 implements CharacterCard {

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
        nInput = 0;
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
        return nInput == 1;
    }
}