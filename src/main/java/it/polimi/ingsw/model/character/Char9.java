package it.polimi.ingsw.model.character;

public class Char9 implements CharacterCard {

    private byte nInput = 0;

    @Override
    public void play() {

    }

    @Override
    public byte getCost() {
        return 1;
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
        return 9;
    }

    @Override
    public boolean canPlay() {
        return (nInput == 2 || nInput == 4);
    }
}

