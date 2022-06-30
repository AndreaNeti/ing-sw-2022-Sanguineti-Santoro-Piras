package it.polimi.ingsw.Util;

import it.polimi.ingsw.Server.model.CharacterCardDataInterface;

public enum CharacterCardData implements CharacterCardDataInterface {
    CH0((byte) 0, (byte) 1, false, true),
    CH1((byte) 1, (byte) 2, false, false),
    CH2((byte) 2, (byte) 3, false, false),
    CH3((byte) 3, (byte) 1, false, false),
    CH4((byte) 4, (byte) 2, false, false),
    CH5((byte) 5, (byte) 3, false, false),
    CH6((byte) 6, (byte) 1, false, true),
    CH7((byte) 7, (byte) 2, false, false),
    CH8((byte) 8, (byte) 3, false, false),
    CH9((byte) 9, (byte) 1, false, false),
    CH10((byte) 10, (byte) 2, false, true),
    CH11((byte) 11, (byte) 3, false, false);
    private final byte id, cost;
    private boolean used;
    private final boolean hasStudents;

    CharacterCardData(byte id, byte cost, boolean used, boolean hasStudents) {
        this.id = id;
        this.cost = cost;
        this.used = used;
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

    public boolean isUsed() {
        return used;
    }

    @Override
    public void setUsed() {
        this.used = true;
    }

    public boolean hasStudents() {
        return hasStudents;
    }
}
