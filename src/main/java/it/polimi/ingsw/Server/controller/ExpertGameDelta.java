package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Enum.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExpertGameDelta extends GameDelta {
    //index of the character, id of the character
    private Map<Byte, Byte> characters;
    // player id, new playerCoinsLeft
    private Map<Byte, Byte> updatedCoinPlayer;
    private Byte newCoinsLeft, newProhibitionsLeft;
    private Color ignoredColorInfluence;

    public ExpertGameDelta() {
        super();
    }

    @Override
    public void clear() {
        super.clear();
        characters = null;
        updatedCoinPlayer = null;
        newCoinsLeft = null;
        newProhibitionsLeft = null;
        ignoredColorInfluence = null;
    }

    @Override
    public void addCharacterCard(byte index, byte id) {
        if (characters == null) {
            characters = new HashMap<>();
        }
        characters.put(index, id);
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    @Override
    public void setUpdatedCoinPlayer(byte playerId, byte newCoinsLeft) {
        if (updatedCoinPlayer == null)
            updatedCoinPlayer = new HashMap<>();
        updatedCoinPlayer.put(playerId, newCoinsLeft);
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    @Override
    public void setNewCoinsLeft(byte newCoinsLeft) {
        this.newCoinsLeft = newCoinsLeft;
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    @Override
    public void setNewProhibitionsLeft(byte newProhibitionsLeft) {
        this.newProhibitionsLeft = newProhibitionsLeft;
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    @Override
    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    @Override
    public Map<Byte, Byte> getCharacters() {
        if (characters == null) return Collections.emptyMap();
        return characters;
    }

    @Override
    public Map<Byte, Byte> getUpdatedCoinPlayer() {
        if (updatedCoinPlayer == null) return Collections.emptyMap();
        return updatedCoinPlayer;
    }

    @Override
    public Optional<Byte> getNewCoinsLeft() {
        return Optional.ofNullable(newCoinsLeft);
    }

    @Override
    public Optional<Byte> getNewProhibitionsLeft() {
        return Optional.ofNullable(newProhibitionsLeft);
    }

    @Override
    public Optional<Color> getIgnoredColorInfluence() {
        return Optional.ofNullable(ignoredColorInfluence);
    }
}
