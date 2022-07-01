package it.polimi.ingsw.server.model.GameComponents;

import it.polimi.ingsw.utils.HouseColor;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * Island class represents the game's islands, where students and towers can be placed.
 */
public class Island extends GameComponent {
    private HouseColor team;
    //prohibition is the representation of the NO Entry Tiles which avoids the calculation of the influence on an island
    private byte prohibition;
    //it's the number of the island merged in this island
    private byte archipelagoSize;

    /**
     * Constructor Island creates a new instance of Island.
     *
     * @param idGameComponent of type {@code byte} - unique ID to assign to the island.
     */
    public Island(byte idGameComponent) {
        super(idGameComponent);
        team = null;
        prohibition = 0;
        archipelagoSize = 1;
    }

    /**
     * Method getTeamColor returns the team controlling the island.
     *
     * @return {@link HouseColor} - color of the team controlling the island.
     */
    public HouseColor getTeamColor() {
        return team;
    }

    /**
     * Method setTeamColor updates the team controlling the island.
     *
     * @param teamColor of type {@link HouseColor} - color of the new team controlling the island.
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
        this.archipelagoSize += island.getArchipelagoSize();
        addProhibitions(island.getProhibitions());
    }

    /**
     * Method getArchipelagoSize returns the size of the archipelago of the island. <br>
     * The size corresponds to the amount of towers placeable when a team controls the island.
     *
     * @return {@code byte} - total number of islands in the archipelago.
     */
    public byte getArchipelagoSize() {
        return archipelagoSize;
    }

    /**
     * Method getProhibitions returns the number of prohibitions placed on the island. <br>
     * This function should be called only during expert games.
     *
     * @return {@code byte} - number of prohibitions.
     */
    public byte getProhibitions() {
        return prohibition;
    }

    /**
     * Method addProhibitions add one prohibition or more (when merging islands with prohibitions) to the island. <br>
     * This function should be called only during expert games.
     *
     * @param value of type {@code byte} - amount of prohibitions to add.
     */
    public void addProhibitions(byte value) {
        if (value < 0) throw new IllegalArgumentException("Cannot add negative prohibitions");
        else prohibition += value;
    }

    /**
     * Method removeProhibition removes a prohibition to the island. <br>
     * This function should be called only during expert games.
     */
    public void removeProhibition() {
        if (prohibition > 0)
            prohibition--;
    }
}
