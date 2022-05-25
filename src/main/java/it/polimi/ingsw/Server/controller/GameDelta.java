package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Server.model.Island;
import it.polimi.ingsw.network.toClientMessage.DeltaUpdate;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

import java.io.Serializable;
import java.util.*;

public class GameDelta implements Serializable {
    private final transient ArrayList<GameListener> listeners;
    // GC index, students array
    private Map<Byte, GameComponent> updatedGC;
    // deleted islands ids
    private Set<Byte> deletedIslands;

    // professor color, new wizard controlling
    private Map<Color, Wizard> updatedProfessors;
    // towers left
    private Map<HouseColor, Byte> newTeamTowersLeft;
    private Byte newMotherNaturePosition, playedCard;
    private transient boolean automaticSending;

    public GameDelta() {
        listeners = new ArrayList<>();
        clear();
        automaticSending = false;
    }

    public void clear() {
        updatedGC = null;
        deletedIslands = null;
        newTeamTowersLeft = null;
        updatedProfessors = null;
        newMotherNaturePosition = null;
        playedCard = null;
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

    public void send() {
        ToClientMessage m = new DeltaUpdate(this);
        for (GameListener listener : listeners)
            listener.update(m);
        clear();
        automaticSending = true;
    }

    public boolean isAutomaticSending() {
        return automaticSending;
    }

    public void addCharacterCard(byte index, byte id) {
    }

    public void setUpdatedCoinPlayer(byte playerId, byte newCoinsPlayer) {
    }

    public void setNewCoinsLeft(byte newCoinsLeft) {
    }

    public void setNewProhibitionsLeft(byte newProhibitionsLeft) {
    }

    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
    }

    public void setUsedCharacter(byte charId, boolean used) {
    }

    public List<Byte> getCharacters() {
        return new ArrayList<>();
    }

    public Map<Byte, Byte> getUpdatedCoinPlayer() {
        return Collections.emptyMap();
    }

    public Optional<Byte> getNewCoinsLeft() {
        return Optional.empty();
    }

    public Optional<Byte> getNewProhibitionsLeft() {
        return Optional.empty();
    }

    public Optional<Color> getIgnoredColorInfluence() {
        return Optional.empty();
    }

    public Map<Byte, Boolean> getUpdatedCharacter() {
        return Collections.emptyMap();
    }

    public Map<Byte, GameComponent> getUpdatedGC() {
        if (updatedGC == null) return Collections.emptyMap();
        return updatedGC;
    }

    public Optional<Byte> getNewMotherNaturePosition() {
        return Optional.ofNullable(newMotherNaturePosition);
    }

    public Map<Color, Wizard> getUpdatedProfessors() {
        if (updatedProfessors == null) return Collections.emptyMap();
        return updatedProfessors;
    }

    public Optional<Byte> getPlayedCard() {
        return Optional.ofNullable(playedCard);
    }

    public Set<Byte> getDeletedIslands() {
        if (deletedIslands == null) return Collections.emptySet();
        return deletedIslands;
    }

    public Map<HouseColor, Byte> getNewTeamTowersLeft() {
        if (newTeamTowersLeft == null) return Collections.emptyMap();
        return newTeamTowersLeft;
    }

    @Override
    public String toString() {
        return "GameDelta{" +
                "listeners=" + listeners +
                ", updatedGC=" + updatedGC +
                ", deletedIslands=" + deletedIslands +
                ", updatedProfessors=" + updatedProfessors +
                ", newTeamTowersLeft=" + newTeamTowersLeft +
                ", newMotherNaturePosition=" + newMotherNaturePosition +
                ", playedCard=" + playedCard +
                ", automaticSending=" + automaticSending +
                '}';
    }
}
