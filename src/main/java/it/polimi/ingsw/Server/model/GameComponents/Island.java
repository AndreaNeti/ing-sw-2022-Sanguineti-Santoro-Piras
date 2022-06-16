package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * Island class represents the game's islands, where students and towers can be placed.
 */
public class Island extends GameComponent {
    private HouseColor team;
    //prohibition is the representation of the NO Entry Tiles which avoids the calculation of the influence on an island
    private byte prohibition;
    //it's the number of the island merged in this island
    private byte number;

    /**
     * Constructor Island creates a new instance of Island.
     *
     * @param idGameComponent of type {@code byte} - unique ID to assign to the island.
     */
    public Island(byte idGameComponent) {
        super(idGameComponent);
        team = null;
        prohibition = 0;
        number = 1;
    }

    /**
     * Method getTeamColor returns the team controlling the island.
     *
     * @return {@link HouseColor} - team color controlling the island.
     */
    public HouseColor getTeamColor() {
        return team;
    }

    /**
     * Method setTeamColor updates the team controlling the island.
     *
     * @param teamColor of type {@link HouseColor} - the new team color controlling the island.
     */
    public void setTeamColor(HouseColor teamColor) {
        if (teamColor == null) throw new IllegalArgumentException("Cannot set null house color");
        this.team = teamColor;
    }

    //merge function is used to send all the students from another island to this: the try catch block is due to the fact that an island knows
    //exactly how many students are present, and therefore it will never throw the exception

    /**
     * Method merge moves all students and towers from a source island to this island.
     *
     * @param island of type {@link Island} - instance of the source island.
     */
    public void merge(Island island) {
        if (island == null) throw new IllegalArgumentException("Cannot merge to null island");
        try {
            island.moveAll(this);
        } catch (NotAllowedException ignored) {

        }
        this.number += island.getNumber();
        addProhibitions(island.getProhibitions());
    }

    /**
     * Method getNumber returns the number of towers that the island will contain if controlled.
     *
     * @return {@code byte} - total number of towers.
     */
    public byte getNumber() {
        return number;
    }

    /**
     * Method getProhibitions returns the number of prohibitions placed on the island.
     * This function should be called only during expert games.
     *
     * @return {@code byte} - number of prohibitions.
     */
    public byte getProhibitions() {
        return prohibition;
    }

    /**
     * Method addProhibitions add one prohibition or more (when merging islands with prohibitions) to the island.
     * This function should be called only during expert games.
     *
     * @param value of type {@code byte} - amount of prohibitions to add.
     */
    public void addProhibitions(byte value) {
        if (value < 0) throw new IllegalArgumentException("Cannot add negative prohibitions");
        else prohibition += value;
    }

    /**
     * Method removeProhibition removes a prohibition to the island.
     * This function should be called only during expert games.
     */
    public void removeProhibition() {
        if (prohibition > 0)
            prohibition--;
    }
}
