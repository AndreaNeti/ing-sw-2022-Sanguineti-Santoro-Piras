package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamClient {

    private final HouseColor houseColor;
    private byte towersLeft;

    private final ArrayList<PlayerClient> membersClient;

    public TeamClient(HouseColor teamColor, List<Player> members, MatchConstants matchConstants) {
        this.houseColor = teamColor;
        this.towersLeft = (byte) matchConstants.towersForTeam();
        membersClient = new ArrayList<>();
        for (Player p : members)
            membersClient.add(new PlayerClient(p, matchConstants));
    }

    public ArrayList<PlayerClient> getPlayers() {
        return membersClient;
    }

    public void addPlayer(PlayerClient p) {
        if (p == null) throw new IllegalArgumentException("Adding null player");
        if (membersClient.contains(p)) throw new IllegalArgumentException("Player already in the team");
        membersClient.add(p);
    }

    public byte getTowersLeft() {
        return towersLeft;
    }

    protected void setTowersLeft(byte towersLeft) {
        this.towersLeft = towersLeft;
    }

    public HouseColor getHouseColor() {
        return houseColor;
    }

    @Override
    public String toString() {
        return houseColor.toString() + ": " + getPlayers().toString();
    }
}
