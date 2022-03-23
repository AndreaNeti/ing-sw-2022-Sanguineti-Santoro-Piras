package it.polimi.ingsw.model.character;

public class Char4 implements Character {
    private byte prohibitionsLeft = 4;

    @Override
    public void play() {
        prohibitionsLeft--;
    }

    @Override
    public byte getCost() {
        return 2;
    }
}