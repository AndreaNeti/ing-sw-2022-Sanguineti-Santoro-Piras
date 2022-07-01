package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.HouseColor;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Team class represents the team logic of "Eriantys". If the game has 2 or 3 players, the team will coincide
 * with the single player. If the game has 4 players there will be 2 teams of 2 players each.
 * Each team has its unique house color, number of members and number of towers.
 */
public class Team implements Serializable {
    private final HouseColor houseColor;
    private final ArrayList<Player> members;
    private transient final byte teamSize;
    private transient final byte maxTowers;
    private transient byte towersLeft;

    /**
     * Constructor Team creates a new instance of Team.
     *
     * @param houseColor of type {@link HouseColor} - house color of the team.
     * @param teamSize of type {@code byte} - number of players in the team.
     * @param maxTowers of type {@code byte} - total number of towers in the team.
     */
    public Team(HouseColor houseColor, byte teamSize, byte maxTowers) {
        if (houseColor == null) throw new IllegalArgumentException("Null house color");
        this.houseColor = houseColor;
        this.teamSize = teamSize;
        this.maxTowers = maxTowers;
        this.members = new ArrayList<>(teamSize);
        this.towersLeft = maxTowers;
    }

    /**
     * Method getPlayers returns all the players in the team.
     *
     * @return {@code ArrayList}<{@link Player}> - list of the instances of the team's players.
     */
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(members);
    }

    /**
     * Method addPlayer adds the selected player to the team.
     *
     * @param player of type {@link Player} - instance of the player to add.
     * @throws NotAllowedException if the team is full or the player is already inside the team.
     */
    protected void addPlayer(Player player) throws NotAllowedException {
        if (player == null) throw new IllegalArgumentException("Adding null player");
        if (isFull()) throw new NotAllowedException("Team is already full");
        if (members.contains(player)) throw new NotAllowedException("Player already in the team");
        members.add(player);
    }

    /**
     * Method getHouseColor returns the house color of the team.
     *
     * @return {@link HouseColor} - house color of the team.
     */
    public HouseColor getHouseColor() {
        return houseColor;
    }

    /**
     * Method isFull checks if the team is full.
     *
     * @return {@code boolean} - true if the number of players in the team is equal to the team size, false else.
     */
    public boolean isFull() {
        return teamSize == members.size();
    }

    /**
     * Method getTowersLeft returns the number of towers left in the team.
     *
     * @return {@code byte} - number of towers left.
     */
    public byte getTowersLeft() {
        return this.towersLeft;
    }

    /**
     * Method addTowers adds a selected amount of towers to the team.
     *
     * @param towers of type {@code byte} - number of towers to add.
     */
    public void addTowers(byte towers) {
        if (towers < 0) throw new IllegalArgumentException("Cannot add negative towers");
        if (towersLeft + towers > maxTowers) throw new IllegalArgumentException("Max towers exceeded");
        towersLeft += towers;
    }

    /**
     * Method removeTowers removes a selected amount of towers to the team.
     *
     * @param towers of type {@code byte} - number of towers to remove.
     * @throws EndGameException if after removing the towers there are none left.
     */
    public void removeTowers(byte towers) throws EndGameException {
        if (towers < 0) throw new IllegalArgumentException("Cannot remove negative towers");
        if (towers > 0) {
            if (towersLeft > towers)
                towersLeft -= towers;
            else {
                towersLeft = 0;
                throw new EndGameException(true);
            }
        }
    }

    /**
     * Method equals is used to compare two Teams, based on their house color.
     *
     * @param o of type {@code Object} - instance of the other Object.
     * @return {@code boolean} - true if the other object is a Team and has the same house color of the team.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return houseColor == team.houseColor;
    }

    /**
     * Method hasCode returns the hash code obtained from the team's house color.
     *
     * @return int - hash code of the team's house color.
     */
    @Override
    public int hashCode() {
        return Objects.hash(houseColor);
    }

    /**
     * Method toString returns the house color of the team.
     *
     * @return {@code String} - "(HouseColor) team".
     */
    @Override
    public String toString() {
        return houseColor.name().toLowerCase() + " team";
    }
}
