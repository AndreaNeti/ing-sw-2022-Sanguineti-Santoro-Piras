package it.polimi.ingsw.model;

public class Island extends GameComponent {
    private Team team;
    //prohibition is the representation of the NO Entry Tiles which avoids the calculation of the influence on an island
    private byte prohibition;
    //it's the number of the island merged in this island
    private byte number;

    public Island() {
        super();
        team = null;
        prohibition = 0;
        number = 1;
    }
    // TODO by copy?
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    //merge function is used to send all the students from another island to this: the try catch block is due to the fact that an island knows
    //exactly how many students are present, and therefore it will never throw the exception

    public void merge(Island island) {
        if (island != null) {
            island.moveAll(this);
            this.number += island.getNumber();
            addProhibitions(island.getProhibitions());
        } else System.err.println("Cannot merge to null island");
    }

    public byte getNumber() {
        return number;
    }

    public byte getProhibitions() {
        return prohibition;
    }

    public void addProhibitions(byte value) {
        prohibition += value;
    }

    public void removeProhibition() {
        if (prohibition > 0)
            prohibition--;
    }
}
