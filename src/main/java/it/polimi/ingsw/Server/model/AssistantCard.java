package it.polimi.ingsw.Server.model;

import java.io.Serializable;

public class AssistantCard implements Serializable {
    private final byte value;
    private final byte moves;
    private boolean used;

    public AssistantCard(byte value, byte moves) {
        this.value = value;
        this.moves = moves;
        this.used = false;
    }

    public byte getMoves() {
        return moves;
    }

    public byte getValue() {
        return value;
    }

    public void setUsed() {
        this.used = true;
    }

    public boolean isUsed() {
        return used;
    }
}
