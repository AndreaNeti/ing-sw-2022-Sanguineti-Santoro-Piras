package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;

import java.util.Map;

public class ExpertGame extends Game {
    private byte coinsLeft;
    private Map<Character, Boolean> characters;
    private boolean extraInfluence; //di default falso
    private boolean towerInfluence;// default true
    private boolean extraSteps; //default false
    private Color ignoredColorInfluence;

    private void addCoinsToPlayer(ExpertPlayer player, byte coins) throws NotEnoughCoinsException {
        if (coinsLeft == 0) throw new NotEnoughCoinsException();
        else if (coinsLeft < coins) {
            player.addCoins(coinsLeft);
            coinsLeft = 0;
        } else {
            player.addCoins(coins);
            coinsLeft -= coins;
        }
    }

    private void addCoins(byte coins) {
        this.coinsLeft += coins;
    }

    public boolean isExtraInfluence() {
        return extraInfluence;
    }

    public void setExtraInfluence(boolean extraInfluence) {
        this.extraInfluence = extraInfluence;
    }

    public boolean isTowerInfluence() {
        return towerInfluence;
    }

    public void setTowerInfluence(boolean towerInfluence) {
        this.towerInfluence = towerInfluence;
    }

    public boolean isExtraSteps() {
        return extraSteps;
    }

    public void setExtraSteps(boolean extraSteps) {
        this.extraSteps = extraSteps;
    }

    public Color getIgnoredColorInfluence() {
        return ignoredColorInfluence;
    }

    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
    }

}
