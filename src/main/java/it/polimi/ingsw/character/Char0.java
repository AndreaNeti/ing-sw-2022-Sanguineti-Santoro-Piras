package it.polimi.ingsw.character;

public class Char0 implements Character {
    private byte prohibitionsLeft = 4;

    @Override
    public void play() {
        prohibitionsLeft--;
    }

    @Override
    public byte getCost() {
        return 1;
    }
}
