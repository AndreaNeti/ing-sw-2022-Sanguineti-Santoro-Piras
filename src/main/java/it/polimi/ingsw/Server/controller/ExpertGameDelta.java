package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Enum.Color;

import java.util.*;

public class ExpertGameDelta extends GameDelta {
    //index of the character, id of the character
    private List<Byte> characters;
    // player id, new playerCoinsLeft
    private Map<Byte, Byte> updatedCoinPlayer;
    private Byte newCoinsLeft, newProhibitionsLeft;
    private Color ignoredColorInfluence;
    //charId, used
    private Map<Byte, Boolean> usedCharacter;

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
        usedCharacter = null;
    }

    @Override
    public void addCharacterCard(byte index, byte id) {
        if (characters == null)
            characters = new ArrayList<>();
        characters.add(index, id);
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

    public void setUsedCharacter(byte charId, boolean used) {
        if (usedCharacter == null)
            usedCharacter = new HashMap<>();
        usedCharacter.put(charId, used);
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
    public List<Byte> getCharacters() {
        if (characters == null) return new ArrayList<>();
        return characters;
    }

    @Override
    public Map<Byte, Byte> getUpdatedCoinPlayer() {
        if (updatedCoinPlayer == null) return Collections.emptyMap();
        return updatedCoinPlayer;
    }

    public Map<Byte, Boolean> getUsedCharacter() {
        if (usedCharacter == null) return Collections.emptyMap();
        return usedCharacter;
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
