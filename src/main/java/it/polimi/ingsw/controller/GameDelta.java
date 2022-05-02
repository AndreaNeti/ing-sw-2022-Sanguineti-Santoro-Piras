package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.DeltaUpdate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameDelta implements Serializable {

    // TODO add update() everywhere
    private transient final GameListener listener;
    // GC index, students array
    private HashMap<Byte, byte[]> updatedGC;
    private HashMap<Byte, IslandData> updatedIslandData;
    private HashMap<Wizard, byte[]> updatedEntranceHall;
    private HashMap<Wizard,byte[]> updatedLunchHall;
    // deleted islands ids
    private Set<Byte> deletedIslands;

    // professor color, new wizard controlling
    private HashMap<Color, Wizard> updatedProfessors;
    private HashMap<Wizard,HouseColor> members;
    // towers left
    private HashMap<HouseColor, Byte> newTeamTowersLeft;
    private Byte newCurrentPlayer, newMotherNaturePosition, playedCard;
    private boolean automaticSending; //default true

    public GameDelta(GameListener listener) {
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
        automaticSending=true;
    }

    public void addUpdatedGC(GameComponent gcUpdated) {
        if (updatedGC == null)
            updatedGC = new HashMap<>();
        byte[] students = new byte[Color.values().length];
        for (byte i = 0; i < Color.values().length; i++)
            students[i] = gcUpdated.howManyStudents(Color.values()[i]);

        updatedGC.put(gcUpdated.getId(), students);
        if(automaticSending)
            send();
    }
    public void addMember(Wizard wizard,HouseColor houseColor){
        if(members==null)
            members=new HashMap<>();
        members.put(wizard,houseColor);
        if(automaticSending)
            send();
    }
    public void addUpdatedEntranceHall(Wizard wizard,EntranceHall entranceHall){
        if(updatedEntranceHall==null)
            updatedEntranceHall=new HashMap<>();
        byte[] students = new byte[Color.values().length];
        for (byte i = 0; i < Color.values().length; i++)
            students[i] = entranceHall.howManyStudents(Color.values()[i]);
        updatedEntranceHall.put(wizard,students);
        if(automaticSending)
            send();
    }
    public void addUpdatedLunchHall(Wizard wizard,LunchHall lunchHall){
        if( updatedLunchHall==null)
            updatedLunchHall=new HashMap<>();
        byte[] students = new byte[Color.values().length];
        for (byte i = 0; i < Color.values().length; i++)
            students[i] = lunchHall.howManyStudents(Color.values()[i]);
        updatedLunchHall.put(wizard,students);
        if(automaticSending)
            send();
    }
    public void addUpdatedGC(Island updatedIsland) {
        addUpdatedGC((GameComponent) updatedIsland);
        if (updatedIslandData == null)
            updatedIslandData = new HashMap<>();
        updatedIslandData.put(updatedIsland.getId(), new IslandData(updatedIsland.getProhibitions(), updatedIsland.getTeamColor()));
        if (automaticSending)
            send();
    }

    public void addDeletedIslands(Island deletedIsland) {
        if (deletedIslands == null)
            deletedIslands = new HashSet<>();
        deletedIslands.add(deletedIsland.getId());
        if (automaticSending)
            send();
    }

    public void updateTeamTowersLeft(HouseColor teamColor, byte newTowersLeft) {
        if (newTeamTowersLeft == null)
            newTeamTowersLeft = new HashMap<>();
        newTeamTowersLeft.put(teamColor, newTowersLeft);
        if (automaticSending)
            send();
    }

    public void addUpdatedProfessors(Color professorColor, Wizard newController) {
        if (updatedProfessors == null)
            updatedProfessors = new HashMap<>();
        updatedProfessors.put(professorColor, newController);
        if (automaticSending)
            send();
    }

    public void setNewCurrentPlayer(byte newCurrentPlayer) {
        this.newCurrentPlayer = newCurrentPlayer;
        if (automaticSending)
            send();
    }

    public void setNewMotherNaturePosition(byte newMotherNaturePosition) {
        this.newMotherNaturePosition = newMotherNaturePosition;
        if (automaticSending)
            send();
    }

    public void setPlayedCard(byte playedCard) {
        this.playedCard = playedCard;
        if (automaticSending)
            send();
    }

    public void setAutomaticSending(boolean automaticSending) {
        this.automaticSending = automaticSending;
    }

    private record IslandData(byte prohibitions, HouseColor team) {
    }

    public void send() {
        listener.update(new DeltaUpdate(this));
        this.clear();
    }
    public boolean isAutomaticSending(){
        return automaticSending;
    }
}
