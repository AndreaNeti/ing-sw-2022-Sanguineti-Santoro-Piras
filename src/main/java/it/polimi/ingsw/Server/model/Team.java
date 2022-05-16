package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Team implements Serializable {
    private final HouseColor houseColor;
    private final ArrayList<Player> members;
    private transient final byte teamSize;
    private transient final byte maxTowers;
    private transient byte towersLeft;

    public Team(HouseColor hc, byte teamSize, byte maxTowers) {
        if (hc == null) throw new IllegalArgumentException("Null house color");
        this.houseColor = hc;
        this.teamSize = teamSize;
        this.maxTowers = maxTowers;
        this.members = new ArrayList<>(teamSize);
        this.towersLeft = maxTowers;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(members);
    }

    protected void addPlayer(Player p) throws NotAllowedException {
        if (p == null) throw new IllegalArgumentException("Adding null player");
        if (isFull()) throw new NotAllowedException("Team is already full");
        if (members.contains(p)) throw new NotAllowedException("Player already in the team");
        members.add(p);
    }

    /*
        public void removePlayer(Player p) throws NotAllowedException {
            if (p != null) {
                if (!members.remove(p)) throw new NotAllowedException("Player not present");
            } else
                System.err.println("Player cannot be null");

        }
    */
    public HouseColor getHouseColor() {
        return houseColor;
    }

    /*
        public void movePlayer(Player p, Team t) throws NotAllowedException {
            if (p != null && t != null && !t.isFull()) {
                removePlayer(p);
                t.addPlayer(p);
            } else
                System.err.println("Player and Team cannot be null");
        }
    */
    public boolean isFull() {
        return teamSize == members.size();
    }

    public byte getTowersLeft() {
        return this.towersLeft;
    }

    public void addTowers(byte b) {
        if (b < 0) throw new IllegalArgumentException("Cannot add negative towers");
        if (towersLeft + b > maxTowers) throw new IllegalArgumentException("Max towers exceeded");
        towersLeft += b;
    }

    public void removeTowers(byte b) throws EndGameException {
        if (b < 0) throw new IllegalArgumentException("Cannot remove negative towers");
        if (b > 0) {
            if (towersLeft > b)
                towersLeft -= b;
            else {
                towersLeft = 0;
                throw new EndGameException(true);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return houseColor == team.houseColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseColor);
    }

    @Override
    public String toString() {
        return houseColor.name().toLowerCase() + " team";
    }
}