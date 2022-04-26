package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;

import java.util.HashMap;

public class ExpertGameDelta extends GameDelta {
    // player id, new playerCoinsLeft
    private HashMap<Byte, Byte> updatedCoinPlayer;
    private Byte newCoinsLeft, newProhibitionsLeft;
    private Color ignoredColorInfluence;

    @Override
    public void clear() {
        super.clear();
        updatedCoinPlayer = null;
        newCoinsLeft = null;
        newProhibitionsLeft = null;
        ignoredColorInfluence = null;
    }

    public void setUpdatedCoinPlayer(byte playerId, byte newCoinsLeft) {
        if (updatedCoinPlayer == null)
            updatedCoinPlayer = new HashMap<>();
        updatedCoinPlayer.put(playerId, newCoinsLeft);
    }

    public void setNewCoinsLeft(byte newCoinsLeft) {
        this.newCoinsLeft = newCoinsLeft;
    }

    public void setNewProhibitionsLeft(byte newProhibitionsLeft) {
        this.newProhibitionsLeft = newProhibitionsLeft;
    }

    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
    }
}
