package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;

public class ExpertPlayer extends Player {
    private byte coins = 0;

    public ExpertPlayer(Wizard wizard, byte tower, String nickName, EntranceHall entranceHall, LunchHall lunchHall) {
        super(wizard, tower, nickName, entranceHall, lunchHall);
    }

    public void addCoins(byte coins) {
        this.coins += coins;
    }

    public void removeCoins(byte coins) throws NotEnoughCoinsException {
        if (coins > this.coins) throw new NotEnoughCoinsException();
        this.coins -= coins;
    }

    public byte getCoins() {
        return coins;
    }
}
