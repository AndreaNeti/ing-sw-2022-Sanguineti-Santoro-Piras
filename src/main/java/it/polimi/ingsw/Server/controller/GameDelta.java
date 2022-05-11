package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.model.*;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.network.toClientMessage.DeltaUpdate;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

import java.io.Serializable;
import java.util.*;

public class GameDelta implements Serializable {
    private final transient ArrayList<GameListener> listeners;
    // GC index, students array
    private HashMap<Byte, GameComponent> updatedGC;
    // deleted islands ids
    private Set<Byte> deletedIslands;

    // professor color, new wizard controlling
    private HashMap<Color, Wizard> updatedProfessors;
    // towers left
    private HashMap<HouseColor, Byte> newTeamTowersLeft;
    private Byte newMotherNaturePosition, playedCard;
    private transient boolean automaticSending;

    public GameDelta() {
        listeners = new ArrayList<>();
        automaticSending = false;
    }

    public void clear() {
        updatedGC=null;
        deletedIslands = null;
        newTeamTowersLeft = null;
        updatedProfessors = null;
        newMotherNaturePosition = null;
        playedCard = null;
        automaticSending = true;
    }

    public void addUpdatedGC(GameComponent gcUpdated) {
        if (updatedGC == null)
            updatedGC = new HashMap<>();
        updatedGC.put(gcUpdated.getId(), gcUpdated);
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

    public void addListener(GameListener gameListener) {
        listeners.add(gameListener);
    }

    public void send()  {
        ToClientMessage m = new DeltaUpdate(this);
        for (GameListener listener : listeners)
            listener.update(m);
        this.clear();
    }

    public boolean isAutomaticSending() {
        return automaticSending;
    }

    public void addCharacter(byte index, byte id){
    }
    public void setUpdatedCoinPlayer(byte playerId, byte newCoindsLeft){}

    public void setNewCoinsLeft(byte newCoinsLeft){}
    public void setNewProhibitionsLeft(byte newProhibitionsLeft){}
    public void setIgnoredColorInfluence(Color ignoredColorInfluence){}

    public HashMap<Byte, GameComponent> getUpdatedGC() {
        return updatedGC;
    }

    public Byte getNewMotherNaturePosition() {
        return newMotherNaturePosition;
    }

    public HashMap<Color, Wizard> getUpdatedProfessors() {
        return updatedProfessors;
    }

    public Byte getPlayedCard() {
        return playedCard;
    }

    public Set<Byte> getDeletedIslands() {
        return deletedIslands;
    }

    public HashMap<HouseColor, Byte> getNewTeamTowersLeft() {
        return newTeamTowersLeft;
    }
}
