package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughCoinsException;

public interface CoinListener {
    void notifyCoins(byte coins) throws NotEnoughCoinsException;
}
