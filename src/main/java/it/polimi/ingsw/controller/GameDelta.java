package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameDelta implements Serializable {

    // TODO add update() everywhere
    private final GameListener listener;

    // GC index, students array
    private HashMap<Byte, byte[]> updatedGC;
    private HashMap<Byte, IslandData> updatedIslandData;
    // deleted islands ids
    private Set<Byte> deletedIslands;
    // team id, towers left and player id, played card
    private HashMap<HouseColor, Byte> newTeamTowersLeft;
    // professor color, new wizard controlling
    private HashMap<Color, Wizard> updatedProfessors;

    private Byte newCurrentPlayer, newMotherNaturePosition, playedCard;

    public GameDelta(GameListener listener){
        this.listener = listener;
    }
    public void clear() {
        updatedGC = null;
        updatedIslandData = null;
        deletedIslands = null;
        newTeamTowersLeft = null;
        updatedProfessors = null;
        newCurrentPlayer = null;
        newMotherNaturePosition = null;
        playedCard = null;
    }

    public void addUpdatedGC(GameComponent gcUpdated) {
        if (updatedGC == null)
            updatedGC = new HashMap<>();
        byte[] students = new byte[Color.values().length];
        for (byte i = 0; i < Color.values().length; i++)
            students[i] = gcUpdated.howManyStudents(Color.values()[i]);

        updatedGC.put(gcUpdated.getId(), students);
    }

    public void addUpdatedGC(Island updatedIsland) {
        addUpdatedGC((GameComponent) updatedIsland);
        if (updatedIslandData == null)
            updatedIslandData = new HashMap<>();
        updatedIslandData.put(updatedIsland.getId(), new IslandData(updatedIsland.getProhibitions(), updatedIsland.getTeamColor()));
    }

    public void addDeletedIslands(Island deletedIsland) {
        if (deletedIslands == null)
            deletedIslands = new HashSet<>();
        deletedIslands.add(deletedIsland.getId());
    }

    public void updateTeamTowersLeft(HouseColor teamColor, byte newTowersLeft) {
        if (newTeamTowersLeft == null)
            newTeamTowersLeft = new HashMap<>();
        newTeamTowersLeft.put(teamColor, newTowersLeft);
    }

    public void addUpdatedProfessors(Color professorColor, Wizard newController) {
        if (updatedProfessors == null)
            updatedProfessors = new HashMap<>();
        updatedProfessors.put(professorColor, newController);
    }

    public void setNewCurrentPlayer(byte newCurrentPlayer) {
        this.newCurrentPlayer = newCurrentPlayer;
    }

    public void setNewMotherNaturePosition(byte newMotherNaturePosition) {
        this.newMotherNaturePosition = newMotherNaturePosition;
    }

    public void setPlayedCard(byte playedCard) {
        this.playedCard = playedCard;
    }

    private record IslandData(byte prohibitions, HouseColor team) {
    }
}
