package it.polimi.ingsw;

public class Card {

    private final byte value, move;


    public Card(byte value, byte move) {
        this.value = value;
        this.move = move;
    }

    public byte getValue() {
        return value;
    }

    public byte getMove() {
        return move;
    }

}
