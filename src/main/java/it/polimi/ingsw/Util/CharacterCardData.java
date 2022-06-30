package it.polimi.ingsw.Util;

import it.polimi.ingsw.Server.model.CharacterCardDataInterface;

public class CharacterCardData implements CharacterCardDataInterface {
    private final byte id, cost;
    private boolean used;
    private final boolean hasStudents, hasProhibitions;

    CharacterCardData(byte id, byte cost, boolean hasStudents, boolean hasProhibitions) {
        this.id = id;
        this.cost = cost;
        this.hasProhibitions = hasProhibitions;
        this.used = false;
        this.hasStudents = hasStudents;
    }

    @Override
    public byte getCharId() {
        return id;
    }

    @Override
    public byte getCost() {
        return used ? (byte) (cost + 1) : cost;
    }

    @Override
    public boolean isUsed() {
        return used;
    }

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public boolean hasStudents() {
        return hasStudents;
    }

    @Override
    public boolean hasProhibitions() {
        return hasProhibitions;
    }
}
