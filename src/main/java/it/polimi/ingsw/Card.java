package it.polimi.ingsw;

import java.util.Objects;

public class Card {

    private final byte value, move;
    private Player player;

    public Card(byte value, byte move, Player player) {
        this.value = value;
        this.move = move;
    }

    public byte getValue() {

        return value;
    }

    public byte getMove() {

        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return value == card.value && move == card.move && Objects.equals(player, card.player);
    }


    @Override
    public int hashCode() {
        return Objects.hash(value, move, player);
    }
}
