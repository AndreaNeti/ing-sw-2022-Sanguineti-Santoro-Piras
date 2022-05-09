package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;

import java.util.HashMap;

public class ExpertGameDelta extends GameDelta {
    //index of the character, id of the character
    private HashMap<Byte, Byte> characters;
    // player id, new playerCoinsLeft
    private HashMap<Byte, Byte> updatedCoinPlayer;
    private Byte newCoinsLeft, newProhibitionsLeft;
    private Color ignoredColorInfluence;

    public ExpertGameDelta() {
        super();
    }

    @Override
    public void clear() {
        super.clear();
        updatedCoinPlayer = null;
        newCoinsLeft = null;
        newProhibitionsLeft = null;
        ignoredColorInfluence = null;
    }

    public void addCharacter(byte index, byte id) {
        if (characters == null) {
            characters = new HashMap<>();
        }
        characters.put(index, id);
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    public void setUpdatedCoinPlayer(byte playerId, byte newCoinsLeft) {
        if (updatedCoinPlayer == null)
            updatedCoinPlayer = new HashMap<>();
        updatedCoinPlayer.put(playerId, newCoinsLeft);
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    public void setNewCoinsLeft(byte newCoinsLeft) {
        this.newCoinsLeft = newCoinsLeft;
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    public void setNewProhibitionsLeft(byte newProhibitionsLeft) {
        this.newProhibitionsLeft = newProhibitionsLeft;
        if (super.isAutomaticSending()) {
            super.send();
        }
    }

    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
        if (super.isAutomaticSending()) {
            super.send();
        }
    }
}
