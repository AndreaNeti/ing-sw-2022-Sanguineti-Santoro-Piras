package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.EndGameException;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private final HouseColor houseColor;
    private byte towersLeft;
    private final List<Player> members;
    private final byte teamSize;
    private final byte maxTowers;

    public Team(HouseColor hc, byte teamSize, byte maxTowers) {
        this.houseColor = hc;
        this.teamSize = teamSize;
        this.maxTowers = maxTowers;
        this.members = new ArrayList<>(teamSize);
        this.towersLeft = maxTowers;
    }

    public List<Player> getPlayers() {
        return this.members;
    }

    public void addPlayer(Player p) throws NotAllowedException {
        if (isFull()) throw new NotAllowedException("Team is already full");
        if (p != null) {
            members.add(p);
        } else
            System.err.println("Player cannot be null");
    }

    public void removePlayer(Player p) {
        if (p != null)
            members.remove(p);
        else
            System.err.println("Player cannot be null");
    }

    public HouseColor getHouseColor() {
        return houseColor;
    }

    public void movePlayer(Player p, Team t) throws NotAllowedException {
        if (p != null && t != null) {
            t.addPlayer(p);
            removePlayer(p);
        } else
            System.err.println("Player and Team cannot be null");
    }

    public boolean isFull() {
        return teamSize == members.size();
    }

    public byte getTowersLeft() {
        return this.towersLeft;
    }

    public void addTowers(byte b) throws NotAllowedException {
        if (b < 0) System.err.println("Cannot add negative towers");
        else if (b > 0) {
            if (towersLeft + b > maxTowers) throw new NotAllowedException("Max towers exceeded");
            towersLeft += b;
        }
    }

    public void removeTowers(byte b) throws EndGameException {
        if (b < 0) System.err.println("Cannot remove negative towers");
        else if (b > 0) {
            towersLeft -= b;
            if (towersLeft <= 0) throw new EndGameException();
        }
    }
}
