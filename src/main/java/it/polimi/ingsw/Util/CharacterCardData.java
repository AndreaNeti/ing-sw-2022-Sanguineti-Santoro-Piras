package it.polimi.ingsw.Util;

/**
 * CharacterCardData
 */
public class CharacterCardData implements CharacterCardDataInterface {
    private final byte id, cost;
    private boolean used;
    private final boolean hasStudents, hasProhibitions;

    /**
     * Constructor CharacterCardData creates a new instance of CharacterCardData.
     *
     * @param id of type {@code byte} - unique ID of the card.
     * @param cost of type {@code byte} - cost of the card.
     * @param hasStudents of type {@code boolean} - boolean to check if the character card can contain students.
     * @param hasProhibitions of type {@code boolean} - boolean to check if the character card contains prohibitions.
     */
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

    /**
     * Method getCost returns the card's cost to play, incremented by 1 if the card has been used.
     *
     * @return {@code byte} - the card's cost
     */
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
