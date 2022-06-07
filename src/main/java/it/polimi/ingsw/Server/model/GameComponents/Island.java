package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

public class Island extends GameComponent {
    private HouseColor team;
    //prohibition is the representation of the NO Entry Tiles which avoids the calculation of the influence on an island
    private byte prohibition;
    //it's the number of the island merged in this island
    private byte number;

    public Island(byte idGameComponent) {
        super(idGameComponent);
        team = null;
        prohibition = 0;
        number = 1;
    }

    public HouseColor getTeamColor() {
        return team;
    }

    public void setTeamColor(HouseColor teamColor) {
        if (teamColor == null) throw new IllegalArgumentException("Cannot set null house color");
        this.team = teamColor;
    }

    //merge function is used to send all the students from another island to this: the try catch block is due to the fact that an island knows
    //exactly how many students are present, and therefore it will never throw the exception

    public void merge(Island island) {
        if (island == null) throw new IllegalArgumentException("Cannot merge to null island");
        try {
            island.moveAll(this);
        } catch (NotAllowedException ignored) {

        }
        this.number += island.getNumber();
        addProhibitions(island.getProhibitions());
    }

    public byte getNumber() {
        return number;
    }

    public byte getProhibitions() {
        return prohibition;
    }

    public void addProhibitions(byte value) {
        if (value < 0) throw new IllegalArgumentException("Cannot add negative prohibitions");
        else prohibition += value;
    }

    public void removeProhibition() {
        if (prohibition > 0)
            prohibition--;
    }
}
