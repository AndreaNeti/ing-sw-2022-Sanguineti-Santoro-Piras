package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.toClientMessage.DeltaUpdate;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameDelta implements Serializable {
    private final transient ArrayList<GameListener> listeners;
    // GC index, students array
    private HashMap<Byte, GameComponent> updatedGC;
    // deleted islands ids
    private Set<Byte> deletedIslands;

    // professor color, new wizard controlling
    private HashMap<Color, Wizard> updatedProfessors;

    // TODO remove from delta and add a notify when player joins the match?
    private HashMap<Wizard, HouseColor> members;
    // towers left
    private HashMap<HouseColor, Byte> newTeamTowersLeft;
    private Byte newCurrentPlayer, newMotherNaturePosition, playedCard;
    private transient boolean automaticSending;

    public GameDelta() {
        listeners = new ArrayList<>();
        automaticSending = false;
    }

    public void clear() {
        updatedGC = null;
        deletedIslands = null;
        newTeamTowersLeft = null;
        updatedProfessors = null;
        newCurrentPlayer = null;
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

    public void addMember(Wizard wizard, HouseColor houseColor) {
        if (members == null)
            members = new HashMap<>();
        members.put(wizard, houseColor);
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

    public void addListener(GameListener gameListener) {
        listeners.add(gameListener);
    }

    public void send() {
        ToClientMessage m = new DeltaUpdate(this);
        for (GameListener listener : listeners)
            listener.update(m);
        this.clear();
    }

    public boolean isAutomaticSending() {
        return automaticSending;
    }
}
