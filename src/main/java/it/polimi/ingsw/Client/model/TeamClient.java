package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.model.Player;
import it.polimi.ingsw.Server.model.Team;
import it.polimi.ingsw.Util.HouseColor;

import java.util.ArrayList;
import java.util.List;

/**
 * TeamClient class represents the team on the client side and corresponds to the server class {@link Team}.
 */
public class TeamClient {

    private final HouseColor houseColor;
    private byte towersLeft;

    private final ArrayList<PlayerClient> membersClient;

    /**
     * Constructor TeamClient creates a new instance of TeamClient.
     *
     * @param teamColor of type {@link HouseColor} - house color of the team.
     * @param members of type {@code List}<{@link Player}> - list of the instances of the player members.
     * @param matchConstants of type {@link MatchConstants} - constant of the team's game.
     */
    public TeamClient(HouseColor teamColor, List<Player> members, MatchConstants matchConstants) {
        this.houseColor = teamColor;
        this.towersLeft = (byte) matchConstants.towersForTeam();
        membersClient = new ArrayList<>();
        for (Player p : members)
            membersClient.add(new PlayerClient(p));
    }

    /**
     * Method getPlayers returns all the players in the team.
     *
     * @return {@code ArrayList}<{@link PlayerClient}> - list of the instances of the team's players.
     */
    public ArrayList<PlayerClient> getPlayers() {
        return membersClient;
    }

    /**
     * Method addPlayer adds the selected player to the team.
     *
     * @param player of type {@link PlayerClient} - instance of the player to add.
     */
    public void addPlayer(PlayerClient player) {
        if (player == null) throw new IllegalArgumentException("Adding null player");
        if (membersClient.contains(player)) throw new IllegalArgumentException("Player already in the team");
        membersClient.add(player);
    }

    /**
     * Method getTowersLeft returns the number of towers left in the team.
     *
     * @return {@code byte} - number of towers left.
     */
    public byte getTowersLeft() {
        return towersLeft;
    }

    /**
     * Method setTowersLeft updates the number of towers left in the team.
     *
     * @param towersLeft of type {@code byte} - new amount of towers left.
     */
    protected void setTowersLeft(byte towersLeft) {
        this.towersLeft = towersLeft;
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
     * Method toString returns the house color of the team and its players.
     *
     * @return {@code String} - "(HouseColor) team: (player1 nickname), (player2 nickname)".
     */
    @Override
    public String toString() {
        StringBuilder team = new StringBuilder(houseColor.toString() + " team: ");
        for (PlayerClient p : getPlayers())
            team.append(p.toString()).append(", ");
        return team.toString();
    }
}
