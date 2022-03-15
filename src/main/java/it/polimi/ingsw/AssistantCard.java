package it.polimi.ingsw;

public class AssistantCard {

    private final int value, move;


    public AssistantCard(int value, int move) {
        this.value = value;
        this.move = move;
    }

    public int getValue() {
        return value;
    }

    public int getMove() {
        return move;
    }

}
